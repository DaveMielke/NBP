includeScriptLibraries command arguments

addCommandOption c configurationDirectory path "the configuration directory" "${defaultConfigurationDirectory}"
addCommandOption d dataDirectory path "the data directory" "${defaultDataDirectory}"

prepareCommonCommandOptions() {
   verifyReadableDirectory "${configurationDirectory}"
   logDebug "configuration-directory ${configurationDirectory}"
   importCommandProperties

   verifyWritableDirectory "${dataDirectory}"
   logDebug "data-directory ${dataDirectory}"
} && readonly -f prepareCommonCommandOptions
