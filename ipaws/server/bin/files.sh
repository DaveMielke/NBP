verifyCommandAvailability stat getfattr setfattr
includeScriptLibraries time

getFileProperty() {
   local path="${1}"
   local property="${2}"

   stat -c "%${property}" -- "${path}"
} && readonly -f getFileProperty

getFileOwnerName() {
   local path="${1}"

   getFileProperty "${path}" U
} && readonly -f getFileOwnerName

getFileOwnerIdentifier() {
   local path="${1}"

   getFileProperty "${path}" u
} && readonly -f getFileOwnerIdentifier

getFileGroupName() {
   local path="${1}"

   getFileProperty "${path}" G
} && readonly -f getFileGroupName

getFileGroupIdentifier() {
   local path="${1}"

   getFileProperty "${path}" g
} && readonly -f getFileGroupIdentifier

getFileType() {
   local path="${1}"

   local type="$(getFileProperty "${path}" F)"
   type="${type#regular }"
   type="${type%% *}"
   echo "${type}"
} && readonly -f getFileType

getFileSize() {
   local path="${1}"

   getFileProperty "${path}" s
} && readonly -f getFileSize

getFileMountPoint() {
   local path="${1}"

   getFileProperty "${path}" m
} && readonly -f getFileMountPoint

getFileAccessedTime() {
   local path="${1}"

   getFileProperty "${path}" X
} && readonly -f getFileAccessedTime

getFileModifiedTime() {
   local path="${1}"

   getFileProperty "${path}" Y
} && readonly -f getFileModifiedTime

getFileChangedTime() {
   local path="${1}"

   getFileProperty "${path}" Z
} && readonly -f getFileChangedTime

setFileTime() {
   local path="${1}"
   local epoch="${2}"
   local which="${3}"

   touch -c -"${which}" -d "$(epochToUTC "${epoch}")" -- "${path}"
} && readonly -f setFileTime

setFileAccessedTime() {
   local path="${1}"
   local epoch="${2}"

   setFileTime "${path}" "${epoch}" a
} && readonly -f setFileAccessedTime

setFileModifiedTime() {
   local path="${1}"
   local epoch="${2}"

   setFileTime "${path}" "${epoch}" m
} && readonly -f setFileModifiedTime

getFileAttribute() {
   local path="${1}"
   local name="${2}"

   getfattr -n "user.${name}" --only-values -- "${path}"
} && readonly -f getFileAttribute

setFileAttribute() {
   local path="${1}"
   local name="${2}"
   local value="${3}"

   setfattr -n "user.${name}" -v "\"${value}\"" -- "${path}"
} && readonly -f setFileAttribute

unsetFileAttribute() {
   local path="${1}"
   local name="${2}"

   setfattr -x "user.${name}" -- "${path}"
} && readonly -f unsetFileAttribute

