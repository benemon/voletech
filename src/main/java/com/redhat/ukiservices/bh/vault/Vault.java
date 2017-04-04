package com.redhat.ukiservices.bh.vault;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Produces({ "text/plain" })
public class Vault {

	private static final Logger LOG = LoggerFactory.getLogger(Vault.class);

	private static final String SINGLE_REQUEST = "Single Vault Key: %s";
	private static final String AGG_REQUEST = "Aggregate Vault Key: %s::%s::%s::%s";

	@Inject
	private VaultService vaultService;

	@GET
	@Path("/vault/{key}")
	public String getValueForKey(@PathParam("key") String key) {

		LOG.info(String.format(SINGLE_REQUEST, key));
		return vaultService.getValueForSingleKey(key);
	}

	@GET
	@Path("/vault/{vault}/{block}/{attribute}/{position}")
	public String getValueForKey(@PathParam("vault") String vault, 
			                     @PathParam("block") String block,
			                     @PathParam("attribute") String attribute, 
			                     @PathParam("position") String position) 
	{

		LOG.info(String.format(AGG_REQUEST, vault, block, attribute, position));
		
		return vaultService.getValueForAggregateKey(vault, block, attribute, position);
	}

}
