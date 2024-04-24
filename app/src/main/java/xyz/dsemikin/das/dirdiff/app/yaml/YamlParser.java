package xyz.dsemikin.das.dirdiff.app.yaml;

import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

public class YamlParser {
    public YamlParser() {
        final LoadSettings loadSettings = LoadSettings.builder().build();
        final Load load = new Load(loadSettings);
        load.loadFromString("asdf");

    }
}
