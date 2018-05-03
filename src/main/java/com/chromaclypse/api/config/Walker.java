package com.chromaclypse.api.config;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.chromaclypse.api.config.visitor.ConfigVisitor;
/**
 * Implementation at {@link com.chromaclypse.lib.WalkerImpl}
 * @author Erik
 *
 */
public interface Walker {
	<T extends ConfigObject> Map<String, Object> serialize(T config);
	<T extends ConfigObject> T deserialize(Class<T> clazz, Map<String, Object> serialData);
	
	void walk(ConfigurationSection root, Object parent, ConfigVisitor visitor);
}
