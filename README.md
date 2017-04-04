# Voletech
A little example at how to use JBoss EAP's Password Vault to avoid storing sensitive information in Environment Variables. The solution is roughly based on [KB370013](https://access.redhat.com/solutions/370013). It has several working parts: 

* The Password Vault files - the vault itself, and the vault keystore. These are provided as examples, based on the contents of [KB2790371](https://access.redhat.com/solutions/2790371). 
  
* *standalone-openshift.xml* - configured to use environment variables to provide the lookup location of the Vault files, as well as the Keystore password.

## Important Note
This process makes use of PicketBox to access the Vault at runtime. This module is slated for deprecation in a future release of EAP. Its replacement, [Wildfly Elytron](https://developer.jboss.org/wiki/WildFlyElytron-ProjectSummary), is aiming for API compatibility with PicketBox, with little or not refactoring required. 

In theory some form of this process will continue to function after PicketBox is deprecated.


## OpenShift

An OpenShift template is provided in the *openshift* directory. This carries out an S2I build on this Git repository, and also contains the secrets necessary for this process to work. These are baked into the template, although you'll want to add your own using OpenShift's Secret function.

To use the template, first create it:

`oc create -f openshift/voletech-openshift-template.yaml`


Then instantiate it:

`oc new-app --template=voletech`



## Instruction

* Add your sensitive data to a vault following the aforementioned KB article. Make a note of the generated key. This will be used in your application.


* Create a secret containing your Vault and its Keystore -  `oc create secret generic vault --from-file=vault.keystore --from-file=vault.dat=VAULT.dat`


* **NOTE** Update the secret once created to reference the correctly capitalised filename - VAULT.dat - otherwise it doesn't function correctly.


* Mount both of these as volumes in your DeploymentConfig. Make a note of where you mounted them - `oc volume dc/voletech --add --name=vault --type=secret --secret-name=vault --mount-path=/deployments/vault`


* Add `VAULT_HOME` environment variable, which defines where your mounted Vault and Keystore are located


* Add `KEYSTORE_PASSWORD` environment variable, matching the information you provided to the Vault.


* Add `KEYSTORE_ALIAS` environment variable, matching the information you provided to the Vault.


* Add `SALT` environment variable, matching the information you provided to the Vault.


* Add `ITERATION_COUNT` environment variable, matching the information you provided to the Vault.


These values should, as a note of good practice, be stored as secrets within the project - but its harder to see what's going on as part of a demo. Which I guess would be the point.

## Usage

A route is created to listen for incoming requests e.g http://voletech-vault.rhel-cdk.10.1.2.2.xip.io/

Two endpoints are created:

* Single requests - `/vault/{key}` - http://voletech-vault.rhel-cdk.10.1.2.2.xip.io/vault/VAULT::vb::password::1

 
* Aggregated requests - `/vault/{vault}/{block}/{attribute}/{position}` - http://voletech-vault.rhel-cdk.10.1.2.2.xip.io/vault/VAULT/vb/password/1 

Sending the above requests to each endpoint will return the same result using the default Vault data supplied in this repository. You'll see the different types of request logged in the Pod logs.

You can use these services to either populate credentials as part of a post-build/pre step, or as part of a more realtime process.








