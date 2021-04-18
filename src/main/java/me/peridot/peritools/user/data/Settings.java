package me.peridot.peritools.user.data;

import lombok.Getter;
import me.peridot.peritools.modifiable.Modifiable;
import me.peridot.peritools.user.User;

public class Settings extends Modifiable {

    @Getter
    private final User user;

    @Getter
    private boolean socialSpy;
    @Getter
    private boolean commandsSpy;
    @Getter
    private boolean god;

    public Settings(User user) {
        this.user = user;
    }

    public void setSocialSpy(boolean socialSpy) {
        this.socialSpy = socialSpy;
        setModified(true);
    }

    public void toggleSocialSpy() {
        setSocialSpy(!this.socialSpy);
    }

    public void setCommandsSpy(boolean commandsSpy) {
        this.commandsSpy = commandsSpy;
        setModified(true);
    }

    public void toggleCommandsSpy() {
        setCommandsSpy(!this.commandsSpy);
    }

    public void setGod(boolean god) {
        this.god = god;
        setModified(true);
    }

    public void toggleGod() {
        setGod(!this.god);
    }

}
