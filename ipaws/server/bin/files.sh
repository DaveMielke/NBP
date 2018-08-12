verifyCommandAvailability stat getfattr setfattr

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

getFileAccessTime() {
   local path="${1}"

   getFileProperty "${path}" X
} && readonly -f getFileAccessTime

getFileModificationTime() {
   local path="${1}"

   getFileProperty "${path}" Y
} && readonly -f getFileModificationTime

getFileChangeTime() {
   local path="${1}"

   getFileProperty "${path}" Z
} && readonly -f getFileChangeTime

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

