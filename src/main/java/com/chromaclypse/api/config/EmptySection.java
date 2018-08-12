package com.chromaclypse.api.config;

import org.bukkit.configuration.ConfigurationSection;

import com.chromaclypse.api.Chroma;

public interface EmptySection extends ConfigurationSection {

	public static EmptySection get() {
		return Chroma.get().factory().instance(EmptySection.class);
	}
}
