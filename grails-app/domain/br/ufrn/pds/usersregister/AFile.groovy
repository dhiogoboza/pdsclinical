package br.ufrn.pds.usersregister

/**
 *
 * @version 1.0
 * @since 1.0
 */
class AFile {

    Long size
    String path
    String name
    String extension
    String contentType
    Date dateCreated
	Date lastUpdated
    Integer downloads
    User user

    static constraints = {
        size min: 0L
        path blank: false
        name blank: false
        extension blank: false
        contentType blank: false
        downloads min: 0
    }

    def afterDelete() {
        try {
            File f = new File(path)
            if (f.delete()) {
                log.debug "file [${path}] deleted"
            } else {
                log.error "could not delete file: ${file}"
            }
        } catch (Exception exp) {
            log.error "Error deleting file: ${e.message}"
            log.error exp
        }
    }
	
	def boolean isImage() {
         contentType ==~ '^image/.*'
    }
}
