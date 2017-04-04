package com.redhat.ukiservices.bh.vault;

import org.jboss.security.vault.SecurityVaultException;
import org.jboss.security.vault.SecurityVaultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaultService {

	private static final Logger LOG = LoggerFactory.getLogger(VaultService.class);
	private static final String ERROR = "Error";
	private static final String DELIMITER = "::";

	/**
	 * Method to open up the Vault with aggregate key
	 * 
	 * @param vault
	 * @param block
	 * @param attribute
	 * @param position
	 * @return String value from this Vault position
	 */
	public String getValueForAggregateKey(String vault, String block, String attribute, String position) {
		StringBuilder builder = new StringBuilder();

		builder.append(vault);
		builder.append(DELIMITER);
		builder.append(block);
		builder.append(DELIMITER);
		builder.append(attribute);
		builder.append(DELIMITER);
		builder.append(position);

		String key = builder.toString();

		return getValue(key);
	}

	/**
	 * Method to open up the Vault with single key
	 * 
	 * @param key
	 * @return
	 */
	public String getValueForSingleKey(String key) {
		return getValue(key);
	}

	/**
	 * Basic method for retrieving a value from the Vault store
	 * 
	 * @param key
	 * @return
	 */
	private String getValue(String key) {
		String value = null;

		try {
			value = SecurityVaultUtil.getValueAsString(key);

		} catch (SecurityVaultException sve) {
			LOG.error(ERROR, sve);
		} catch (IllegalArgumentException iae) {
			LOG.error(ERROR, iae);
		}

		return value;
	}

}
