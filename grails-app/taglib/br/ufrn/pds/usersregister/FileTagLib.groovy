/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.pds.usersregister

class FileTagLib {
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    private static Long inputId = 0;
    
    
    def fileInput = { attr, body ->
        def id = attr.id?:inputId++;
        def url = createLink(controller: "AFile", action: "showImage", id: afile.id);
        def input = "<input type=\"file\" class=\"required\" id=\"applicationFile\" />"
    }

    def image = {attr, body ->
        def src = ""
        def aFile
        
        if(attr.afile) {
            if(attr.afile instanceof AFile) {
                aFile = attr.afile
            } else {
                def id = attr.afile.toString().split(":")[1].replace(" ", "")
                aFile = AFile.get(id)
            }
            src = createLink(controller: "AFile", action: "showImage", id: aFile?.id)
        } else {
            src = attr.src?:""
        }
        
        def attrs = serializeAttrs(attr, ["src", "afile"]);
        
        out << "<img"+attrs+" src='"+src+"'/>"
    }

    def download = {attr, body ->
    }

    def video = {attr, body ->
        def src = ""
        def type = ""

        if (attr.src && attr.type) {
            src = attr.src
            type = attr.type
        } else {
            def afile = null

            if (attr.app) {
                afile = app.video
            } else {
                afile = attr.afile
            }

            if (afile) {
                src = createLink(controller: "aFile", action: "showVideo", id: afile.id)
                type = attr.type ? attr.type : afile.contentType
            }
        }

        def attrs = serializeAttrs(attr, ["src", "type", "app", "afile"]);

        if (src && type) {
            out << "<video" << attrs << ">" << "<source src='" << src << "' type='" << type << "' />" << body() << "</video>"
        } else {
            out << body()
        }
    }
    
    private String serializeAttrs(attr, notIn) {
        def attrs = ""

        attr.each{key, value ->
            if (!(key in notIn)) {
                attrs += " " + key + "='" + value + "'"
            }
        }
        return attrs
    }
}
