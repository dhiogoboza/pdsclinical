class UrlMappings {

	static mappings = {
		
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/AFile/showImage/${id}"(controller:"AFile", action: "showImage")
		
		
		"/"(controller:"home", action:"index")

        
        "500"(view:'/error')
	}
}
