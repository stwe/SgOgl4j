package de.sg.ogl.text;

import de.sg.ogl.resource.Texture;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

public class Font {

    private final Map<Character, FontGlyph> glyphs = new HashMap<>();
    private final Texture texture;
    private int fontHeight;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Font(java.awt.Font awtFont, boolean antialiased) {
        LOGGER.debug("Creates Font object.");

        texture = createFontTexture(Objects.requireNonNull(awtFont, "awtFont must not be null"), antialiased);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public int getTextWidth(CharSequence text) {
        Objects.requireNonNull(text, "text must not be null");

        int width = 0;
        int lineWidth = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;

                continue;
            }

            if (c == '\r') {
                continue;
            }

            lineWidth += glyphs.get(c).width;
        }

        width = Math.max(width, lineWidth);

        return width;
    }

    public int getTextHeight(CharSequence text) {
        Objects.requireNonNull(text, "text must not be null");

        var height = 0;
        var lineHeight = 0;

        for (var i = 0; i < text.length(); i++) {
            var c = text.charAt(i);

            if (c == '\n') {
                height += lineHeight;
                lineHeight = 0;

                continue;
            }

            if (c == '\r') {
                continue;
            }

            lineHeight = Math.max(lineHeight, glyphs.get(c).height);
        }

        height += lineHeight;

        return height;
    }

    //-------------------------------------------------
    // Render
    //-------------------------------------------------

    void render(TextRenderer textRenderer, CharSequence text, float x, float y) {
        Objects.requireNonNull(textRenderer, "textRenderer must not be null");

        float drawX = x;
        float drawY = y;

        Texture.bindForReading(texture.getId(), GL_TEXTURE0);
        textRenderer.begin();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '\n') {
                // line feed, set x and y to draw at the next line
                drawY += fontHeight;
                drawX = x;
                continue;
            }

            if (ch == '\r') {
                // carriage return, just skip it
                continue;
            }

            var glyph = glyphs.get(ch);
            textRenderer.drawTextureRegion(texture, drawX, drawY, glyph.x, glyph.y, glyph.width, glyph.height);
            drawX += glyph.width;
        }

        textRenderer.end();
        Texture.unbind();

        /*
        textRenderer.end():
            - vao bind
            - shader bind
            - update uniforms
            - bind vbo && upload vertex data
            - enable alpha blending
            - glDrawArrays
            - disable blending
            - shader unbind
            - vao unbind
        */
    }

    //-------------------------------------------------
    // Font texture
    //-------------------------------------------------

    private Optional<FontMetrics> getCharFontMetrics(java.awt.Font awtFont, char c, boolean antialiased) {
        var image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        var graphics2D = image.createGraphics();

        if (antialiased) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        graphics2D.setFont(awtFont);
        var fontMetrics = graphics2D.getFontMetrics();
        graphics2D.dispose();

        if (fontMetrics == null) {
            return Optional.empty();
        }

        if (fontMetrics.charWidth(c) == 0) {
            return Optional.empty();
        }

        return Optional.of(fontMetrics);
    }

    private Optional<BufferedImage> createCharImage(java.awt.Font awtFont, char c, boolean antialiased) {
        var fontMetrics = getCharFontMetrics(awtFont, c, antialiased);
        if (fontMetrics.isEmpty()) {
            return Optional.empty();
        }

        var image = new BufferedImage(fontMetrics.get().charWidth(c), fontMetrics.get().getHeight(), BufferedImage.TYPE_INT_ARGB);
        var graphics2D = image.createGraphics();
        if (antialiased) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        graphics2D.setFont(awtFont);
        graphics2D.setPaint(java.awt.Color.WHITE);
        graphics2D.drawString(String.valueOf(c), 0, fontMetrics.get().getAscent());
        graphics2D.dispose();

        return Optional.of(image);
    }

    private Texture createFontTexture(java.awt.Font awtFont, boolean antialiased) {
        LOGGER.debug("Creates a texture from the font {}.", awtFont);

        var imageWidth = 0;
        var imageHeight = 0;

        for (var i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }

            var c = (char) i;
            var charImage = createCharImage(awtFont, c, antialiased);
            if (charImage.isEmpty()) {
                LOGGER.warn("The font {} does not contain the char {}.", awtFont, c);
                continue;
            }

            imageWidth += charImage.get().getWidth();
            imageHeight = Math.max(imageHeight, charImage.get().getHeight());
        }

        fontHeight = imageHeight;

        var image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        var graphics2D = image.createGraphics();

        var x = 0;
        for (var i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }

            var c = (char) i;
            var charImage = createCharImage(awtFont, c, antialiased);
            if (charImage.isEmpty()) {
                continue;
            }

            var charWidth = charImage.get().getWidth();
            var charHeight = charImage.get().getHeight();

            var glyph = new FontGlyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
            graphics2D.drawImage(charImage.get(), x, 0, null);
            x += glyph.width;
            glyphs.put(c, glyph);
        }

        var width = image.getWidth();
        var height = image.getHeight();

        // get pixel data of image
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // put pixel data into a ByteBuffer
        var buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Pixel as RGBA: 0xAARRGGBB
                int pixel = pixels[i * width + j];
                // Red component 0xAARRGGBB >> 16 = 0x0000AARR
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                // Green component 0xAARRGGBB >> 8 = 0x00AARRGG
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                // Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB
                buffer.put((byte) (pixel & 0xFF));
                // Alpha component 0xAARRGGBB >> 24 = 0x000000AA
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        var fontTexture = Texture.createTexture(width, height, buffer);
        MemoryUtil.memFree(buffer);

        return fontTexture;
    }
}
