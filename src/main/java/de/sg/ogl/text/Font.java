package de.sg.ogl.text;

import de.sg.ogl.resource.Texture;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
    // Getter
    //-------------------------------------------------

    public int getWidth(CharSequence text) {
        var width = 0;
        var lineWidth = 0;

        for (var i = 0; i < text.length(); i++) {
            var c = text.charAt(i);
            if (c == '\n') {
                /* Line end, set width to maximum from line width and stored
                 * width */
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (c == '\r') {
                /* Carriage return, just skip it */
                continue;
            }

            var g = glyphs.get(c);
            lineWidth += g.width;
        }

        width = Math.max(width, lineWidth);

        return width;
    }

    public int getHeight(CharSequence text) {
        var height = 0;
        var lineHeight = 0;

        for (var i = 0; i < text.length(); i++) {
            var c = text.charAt(i);
            if (c == '\n') {
                /* Line end, add line height to stored height */
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (c == '\r') {
                /* Carriage return, just skip it */
                continue;
            }

            var g = glyphs.get(c);
            lineHeight = Math.max(lineHeight, g.height);
        }

        height += lineHeight;

        return height;
    }

    //-------------------------------------------------
    // Render
    //-------------------------------------------------

    public void renderText(TextRenderer textRenderer, CharSequence text, float x, float y) {
        int textHeight = getHeight(text);

        float drawX = x;
        float drawY = y;
        if (textHeight > fontHeight) {
            drawY += textHeight - fontHeight;
        }

        Texture.bindForReading(texture.getId(), GL_TEXTURE0);
        textRenderer.begin();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                /* Line feed, set x and y to draw at the next line */
                drawY -= fontHeight;
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            FontGlyph g = glyphs.get(ch);
            textRenderer.drawTextureRegion(texture, drawX, drawY, g.x, g.y, g.width, g.height);
            drawX += g.width;
        }
        textRenderer.end();
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

        // create glyphs
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

        // Flip image Horizontal to get the origin to bottom left
        var transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        var operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);

        // Get charWidth and charHeight of image
        var width = image.getWidth();
        var height = image.getHeight();

        // Get pixel data of image
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // Put pixel data into a ByteBuffer
        var buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                /* Pixel as RGBA: 0xAARRGGBB */
                int pixel = pixels[i * width + j];
                /* Red component 0xAARRGGBB >> 16 = 0x0000AARR */
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                /* Green component 0xAARRGGBB >> 8 = 0x00AARRGG */
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
                buffer.put((byte) (pixel & 0xFF));
                /* Alpha component 0xAARRGGBB >> 24 = 0x000000AA */
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        // Create texture
        var fontTexture = Texture.createTexture(width, height, buffer);
        MemoryUtil.memFree(buffer);

        return fontTexture;
    }
}
