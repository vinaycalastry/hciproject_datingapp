const uploadController = require('../controller/upload_functionality');
const multer = require('multer');
const fs = require('fs');
const imageUploadModel = require('../models/image_db');
var upload = multer({ dest: '/tmp/'});   


module.exports = function(app){
    app.post('/upload/:photoid',upload.single('file'),uploadController.uploadImage);

    app.post('/uploaded', uploadController.getImage);
}