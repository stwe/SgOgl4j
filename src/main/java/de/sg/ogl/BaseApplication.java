package de.sg.ogl;

public abstract class BaseApplication implements Application {

    private SgOglEngine engine;

    public SgOglEngine getEngine() {
        return engine;
    }

    public void setEngine(SgOglEngine engine) {
        this.engine = engine;
    }
}
