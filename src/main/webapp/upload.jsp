<%@ page import="com.google.appengine.api.blobstore.*" %>

<%BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();%>

<!DOCTYPE html>

<html>
    <head>
        <title>Upload file</title>
        <meta charset="utf-8" />
    </head>

    <body>
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            <label>File to send : <input type="file" name="uploadedFile" /></label>
            <br>
            <input type="submit" />
        </form>
    </body>
</html>