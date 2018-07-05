includeScriptLibraries time
verifyCommandAvailability stat

fileProperty() {
   local path="${1}"
   local property="${2}"

   stat -c "%${property}" -- "${path}"
} && readonly -f fileProperty

fileOwnerName() {
   local path="${1}"

   fileProperty "${path}" U
} && readonly -f fileOwnerName

fileOwnerIdentifier() {
   local path="${1}"

   fileProperty "${path}" u
} && readonly -f fileOwnerIdentifier

fileGroupName() {
   local path="${1}"

   fileProperty "${path}" G
} && readonly -f fileGroupName

fileGroupIdentifier() {
   local path="${1}"

   fileProperty "${path}" g
} && readonly -f fileGroupIdentifier

fileType() {
   local path="${1}"

   local type="$(fileProperty "${path}" F)"
   type="${type#regular }"
   type="${type%% *}"
   echo "${type}"
} && readonly -f fileType

fileSize() {
   local path="${1}"

   fileProperty "${path}" s
} && readonly -f fileSize

fileMountPoint() {
   local path="${1}"

   fileProperty "${path}" m
} && readonly -f fileMountPoint

epochFileAccessTime() {
   local path="${1}"

   fileProperty "${path}" X
} && readonly -f epochFileAccessTime

epochFileModificationTime() {
   local path="${1}"

   fileProperty "${path}" Y
} && readonly -f epochFileModificationTime

epochFileChangeTime() {
   local path="${1}"

   fileProperty "${path}" Z
} && readonly -f epochFileChangeTime

utcFileAccessTime() {
   local path="${1}"

   epochToUTC "$(epochFileAccessTime "${path}")"
} && readonly -f utcFileAccessTime

utcFileModificationTime() {
   local path="${1}"

   epochToUTC "$(epochFileModificationTime "${path}")"
} && readonly -f utcFileModificationTime

utcFileChangeTime() {
   local path="${1}"

   epochToUTC "$(epochFileChangeTime "${path}")"
} && readonly -f utcFileChangeTime

