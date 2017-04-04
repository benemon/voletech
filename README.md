# jboss-vaultec
A little example at how to use JBoss EAP's Password Vault to avoid storing sensitive information in Environment Variables. It has several working parts: 

* The Password Vault files - the vault itself, and the vault keystore. These are provided as examples, based on the contents of [this KB Article](https://access.redhat.com/solutions/2790371). 
  
* *standalone-openshift.xml* - configured to use environment variables to provide the lookup location of the Vault files, as well as the Keystore password.

## Important Note
This process makes use of PicketBox to access the Vault at runtime. This module is slated for deprecation in a future release of EAP. Its replacement, [Wildfly Elytron](https://developer.jboss.org/wiki/WildFlyElytron-ProjectSummary), is aiming for API compatibility with PicketBox, with little or not refactoring required. 

In theory some form of this process will continue to function after PicketBox is deprecated.


## OpenShift

An OpenShift template is provided in the *openshift* directory. This carries out an S2I build on this Git repository, and also contains the secrets necessary for this process to work. These are baked into the template, although you'll want to add your own using OpenShift's Secret function.

## Instruction

* Add your sensitive data to a vault following the aforementioned KB article. Make a note of the generated key. This will be used in your application.
* Create a secret containing your Vault and its Keystore -  `oc create secret generic vault --from-file=keystore=vault.keystore --from-file=data=VAULT.dat`
* Mount both of these as volumes in your DeploymentConfig. Make a note of where you mounted them.
* Add `VAULT_HOME` environment variable, which defines where your mounted Vault and Keystore are located
* Add `KEYSTORE_PASSWORD` environment variable, matching the information you provided to the Vault.
* Add `KEYSTORE_ALIAS` environment variable, matching the information you provided to the Vault.
* Add `SALT` environment variable, matching the information you provided to the Vault.
* Add `ITERATION_COUNT` environment variable, matching the information you provided to the Vault.

These values should, as a note of good practice, be stored as secrets within the project - but its harder to see what's going on as part of a demo. Which I guess would be the point.


