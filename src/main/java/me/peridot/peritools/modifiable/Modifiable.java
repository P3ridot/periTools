package me.peridot.peritools.modifiable;

public abstract class Modifiable {

    private boolean modified = false;

    public boolean isModified() {
        return this.modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

}
