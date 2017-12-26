package br.ufrn.pds.usersregister

import static grails.async.Promises.*

//import br.com.dynavideo.nativeimgutils.NativeImgUtils

import grails.transaction.Transactional

import java.nio.file.Files
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import javax.activation.FileTypeMap
import javax.activation.MimetypesFileTypeMap

import org.springframework.web.multipart.*
import org.springframework.web.multipart.commons.*
import org.apache.commons.lang.RandomStringUtils

@Transactional (readOnly = false)
class FilesService {
    def messageSource
    //def fileTypeMap
	FileTypeMap fileTypeMap = new MimetypesFileTypeMap()
	def grailsApplication

    def File saveFile(MultipartFile file, String path) {
        if (file.empty) {
            return null
        } else {
            def fullPath = path+file.getOriginalFilename()
            file.transferTo(new File(fullPath))
            return file
        }
    }
    
	def showImage(Long id, HttpServletRequest request, HttpServletResponse response, int width = 0, int height = 0) {
		def aFile = AFile.get(id)
		if(aFile != null) {
			if(width > 0 || height > 0) {
				showResizedImage(aFile, request, response, width, height)
			} else {
				showFile(aFile, request, response, aFile.isImage())
			}
		}
    }
	
	def showFile(Long id, HttpServletRequest request, HttpServletResponse response) {
		def aFile = AFile.get(id)
		showFile(aFile, request, response, true)
	}
	
	def showFile(AFile aFile, HttpServletRequest request, HttpServletResponse response, boolean showFile) {
        if(aFile != null){
            def path = aFile.path
            def mimetype = aFile.contentType
            _showFile(path, mimetype, request, response, aFile.lastUpdated)
            //aFile.downloads = aFile.downloads + 1
            //aFile.save(flush: true)
        }
    }
    
    def showTmpFile(String path, HttpServletRequest request, HttpServletResponse response){
        
        File file = new File(grailsApplication.config.fileuploader['tmp'].path + path)
        
        _showFile(file, fileTypeMap.getContentType(path.toLowerCase()), request, response, new Date(file.lastModified()))
    }
    
	def showResizedImage(AFile aFile, HttpServletRequest request, HttpServletResponse response, int width, int height) {
		
        def lastModified = aFile.lastUpdated
        def maxAgeSec = grailsApplication.config.cacheControll.maxAge
        def os = response.getOutputStream()
        
        if(request.getHeader("If-None-Match")) {
            if(Long.parseLong(request.getHeader("If-None-Match")) == lastModified.getTime()) {
                response.setStatus(304)
                response.setHeader("Etag", lastModified.getTime().toString())
                os.flush()
                os.close()
                return
            }
        } else if(request.getHeader("If-Modified-Since")) {
            try {
                if(lastModified.compareTo(new Date(request.getHeader("If-Modified-Since"))) <= 0) {
                    response.setStatus(304)
                    response.setHeader("Last-Modified", lastModified.toString())
                    os.flush()
                    os.close()
                    return
                }
            } catch (Exception e) {}
        }
        
        response.setStatus(200)
        response.setHeader("Content-Type", "image/png")
        response.setHeader("Cache-Control", "public,max-age="+maxAgeSec)
        response.setHeader("Last-Modified", lastModified.toString())
        response.setHeader("Etag", lastModified.getTime().toString())
        
        width = width > 0? width : 1
		height = height > 0? height : 1
        
        //NativeImgUtils.resizeImage(aFile.path, width, height, os)
		
        os.flush()
        os.close()
	}
	
    private def _showFile(String path, String mimetype, HttpServletRequest request, HttpServletResponse response, Date lastModified) {
        def file = new File(path)
		_showFile(file, mimetype, request, response, lastModified)
    }
	
	private def _showFile(File file, String mimetype, HttpServletRequest request, HttpServletResponse response, Date lastModified){
		if (file.exists()) {
            _showFile(file.newInputStream(), mimetype, request, response, file.size(),lastModified)
		}
    }
    
    private def _showFile(InputStream inputStream ,  String mimetype, HttpServletRequest request, HttpServletResponse response, Long contentLength, Date lastModified) {
        def maxAgeSec = grailsApplication.config.cacheControll.maxAge
        def os = response.getOutputStream()
        
        def range
        if(request.getHeader("If-None-Match")) {
            if(Long.parseLong(request.getHeader("If-None-Match")).equals(lastModified.getTime())) {
                response.setStatus(304)
                os.flush()
                os.close()
                return
            }
        } else if(request.getHeader("If-Modified-Since")) {
            try {
                if(lastModified.compareTo(new Date(request.getHeader("If-Modified-Since"))) <= 0 ) { 
                    response.setStatus(304)
                    os.flush()
                    os.close()
                    return
                }
            } catch (Exception e) {}
        }
        
        
        
        if(request.getHeader("Range")){
            response.setStatus(206)
            response.setHeader("Accept-Ranges", "bytes")
        }else{
            response.setStatus(200)
        }
        
        response.setHeader("Content-Type", mimetype)
        response.setHeader("Content-Length", contentLength.toString())
        response.setHeader("Cache-Control", "public,max-age="+maxAgeSec)
        response.setHeader("Last-Modified", lastModified.toString())
        response.setHeader("Etag", lastModified.getTime().toString())
        
        if ( (range = request.getHeader("Range") ) != null ) {
            range = range.split("-")[0].replaceAll("\\D+","")
            inputStream.skip(range.toLong());
            response.setHeader( "Content-Range", "bytes "+(range?:0)+"-"+(contentLength -1)+"/"+contentLength)
        }

        
        def len = 0
        byte[] buffer = new byte[1024];

        response.flushBuffer();
        while ((len = inputStream.read(buffer)) > 0) {
            os.write(buffer,0,len);
        }

        inputStream.close();
        os.flush();
        os.close();
    }
    
    def AFile saveFile(String group, File file, User user, String caminho) throws Exception {
        return saveFile(group, file, file.name, user, caminho)
    }
    
    def AFile saveFile(String group, File file, String originalFilename, User user, String caminho, boolean commit = true) throws Exception {
        Locale locale = Locale.getDefault()
        
        // config handler
        def config = grailsApplication.config.fileuploader[group]
        
        /** *********************
        check extensions
         *********************** */
        def fileExtension = file.name.substring(file.name.lastIndexOf('.') + 1)
        if (!config.allowedExtensions[0].equals("*") && !config.allowedExtensions.contains(fileExtension)) {
            def msg = messageSource.getMessage("fileupload.upload.unauthorizedExtension", [fileExtension, config.allowedExtensions] as Object[], locale)
            log.debug msg
            throw new Exception(msg)
        }

        /** *******************
        check file size
         ********************* */
        if (config.maxSize) { //if maxSize config exists
            def maxSizeInKb = ((int) (config.maxSize / 1024))
            if (file.length() > config.maxSize) { //if filesize is bigger than allowed
                log.debug "FileUploader plugin received a file bigger than allowed. Max file size is ${maxSizeInKb} kb"
                def msg = messageSource.getMessage("fileupload.upload.fileBiggerThanAllowed", [maxSizeInKb] as Object[], locale)
                throw new Exception(msg)
            }
        }
        
        //base path to save file
        def path = config.path.toString()
        if (!path.endsWith('/'))
		path = path + "/"
        def name = file.name.contains(".")?file.name.substring(0, file.name.lastIndexOf('.')):file.name
        
        //sets new path
        path = path + group + "/" + caminho + "/"
        if (!new File(path).mkdirs()){
            log.info "FileUploader plugin couldn't create directories: [${path}]"
        }
		//        path = path + name.replace(" ", "_") + "." + fileExtension
		//        println "Caminho:"+path
        
        //move file
        log.debug "FileUploader plugin received a ${file.length()}b file. Moving to ${path}"
        File destiny = new File(path + name + "." + fileExtension)
        def count = 1
        while(destiny.exists()){
            destiny = new File(path + name + " (" + (++count) + ")"+"." + fileExtension)
        }
        path = path + destiny.getName()
        
		if(!file.exists()) {
			return null;
		}
		
		// assync copy the file
		task {
            Files.copy(file.toPath(), destiny.toPath())
        }
        
        //save it on the database
        def afile = new AFile()
        afile.name = originalFilename
        afile.size = destiny.length()
        afile.extension = fileExtension
        afile.path = path
        
        //ufile.class = "com.lucastex.grails.fileuploader.UFile"
        afile.downloads = 0
        afile.user = user
        afile.contentType = fileTypeMap.getContentType(path)
        
        if (commit) {
            if (!afile.save()) {
                println "\n\n\n\nNao salvou o arquivo\n\n\n\n"
            }
        }
        
        return afile 
    }
    
    def AFile saveFile(String group, MultipartFile file, User user, String caminho) throws Exception {
        
        return saveFile(group, (File) saveTempFile(file, user.id), file.originalFilename, user, caminho);
        
    }
    
    def deleteFile(File file){
        task {
            try {
                println "deleting file "+file.absolutePath
                file.delete()
            } catch (SecurityException e) {
                e.printStackTrace()
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }
    
    def getFile(AFile f){
        return new File(f.path)
    }
    
    def getFile(String group, Long userId, String name){
        def config = grailsApplication.config.fileuploader[group]
        def path = config.path.toString()
        if (!path.endsWith('/'))
		path = path + '/'
       
        path += group + '/'
        
        path += userId + '/'
        
        path += name
        
        println 'Getting file in path "' + path + '"'
        
        return new File(path)
    }
  
    def boolean deleteFile(def idUfile) {
        def borro = false;
        def ufile = File.get(idUfile)
        if (!ufile) {
            log.error "could not delete file: ${file}"
            return;
        }
        def file = new java.io.File(ufile.path)
        if (file.exists()) {
			if (file.delete()) {
				log.debug "file [${ufile.path}] deleted"
				borro=true;
			} else {
				log.error "could not delete file: ${file}"
			}
        }
        return borro;
    }
    
    def saveTempFile(MultipartFile file, Long userId){
        def path = grailsApplication.config.fileuploader['tmp'].path.toString()
        
		if (!path.endsWith("/")){
			path = path + "/"
		}
        
        path += userId+"/"
        if (!new File(path).mkdirs())
		log.info "FileUploader plugin couldn't create directories: [${path}]"
        
        path +=file.originalFilename
        
        def dotIndex = path.lastIndexOf(".")
        def name = path[0..dotIndex-1]
        def extension =""
        if(dotIndex >= 0)
         extension = path[dotIndex+1..-1]
        
        File toReturn = new File(path)
        def i = 0;
        while(toReturn.exists())
		toReturn = new File(name+"__"+(i++)+"."+extension)
        
        file.transferTo(toReturn);
        
        return toReturn;
    }
    
    def getTmpFile(Long userId, String fileName){
        def tmpDir = grailsApplication.config.fileuploader['tmp'].path
        if (!tmpDir.endsWith("/"))
		tmpDir = tmpDir + "/"

        return new File(tmpDir+userId+'/'+fileName)
    }
    
    def saveAkpTempFile(MultipartFile file, Long userId){
        def path = grailsApplication.config.fileuploader['apkTmp'].path
        if (!path.endsWith("/"))
		path = path + "/"
        
        path += userId+"/"
        if (!new File(path).mkdirs())
		log.info "FileUploader plugin couldn't create directories: [${path}]"
        
        path +=file.originalFilename;
        
        File toReturn = new File(path)
        
        if(toReturn.exists())
		toReturn.delete();
        
        file.transferTo(toReturn);
        
        return toReturn;
    }
    
    def moveFileToTemp(File file, Long userId) {
        def path = grailsApplication.config.fileuploader['tmp'].path
        if (!path.endsWith("/"))
		path = path + "/"
        
        path += userId+"/"
        if (!new File(path).mkdirs())
		log.info "FileUploader plugin couldn't create directories: [${path}]"
        
        path += file.name
        
        File toReturn = new File(path)
        
        if(toReturn.exists())
		toReturn.delete()
        
        Files.copy(file.toPath(), toReturn.toPath())
        
        file.delete()
        return toReturn
    }
    
}
