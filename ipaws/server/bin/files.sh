includeScriptLibraries time

getFileProperty() {
   local path="${1}"
   local property="${2}"

   stat -c "%${property}" -- "${path}"
} && readonly -f getFileProperty

epochFileAccessTime() {
   local path="${1}"

   getFileProperty "${path}" X
} && readonly -f epochFileAccessTime

epochFileModificationTime() {
   local path="${1}"

   getFileProperty "${path}" Y
} && readonly -f epochFileModificationTime

epochFileChangeTime() {
   local path="${1}"

   getFileProperty "${path}" Z
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

