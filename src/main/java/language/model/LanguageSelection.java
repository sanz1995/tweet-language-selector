package language.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LanguageSelection {

    @Id
    private String language;


    public LanguageSelection() {
    }

    public LanguageSelection(String language) {
        this.language = language;
    }


    public String getLanguage() {
        return language;
    }


}
