const imageUploadModel = require('../models/image_db');
const fs = require('fs');



module.exports.uploadImage = function(req, res){
      var file = __dirname + '/' + req.file.filename;
      fs.rename(req.file.path, file, function(err) {
        if (err) {
          console.log(err);
          res.send(500);
        } else {
                var imageUpload = new imageUploadModel;
                imageUpload.img.data = fs.readFileSync(file);
                imageUpload.img.contentType = 'image/jpg';
                imageUpload.save(function (err, imageUpload) {
                if (err) throw err;
                fs.unlink(req.file.filename);
                console.error('saved img to mongo');
                res.json({
                    message: 'File uploaded successfully',
                    filename: req.file.filename
                });
        });
    }
      });
    }
    
    
module.exports.getImage = function(req, res){
    imageUploadModel.find().exec(function(err, data_op){
        filePath = __dirname + '/data.jpg';
        console.log(filePath);
        fs.appendFile(filePath, data_op[0].img.data, function () {
            res.json({end: "ok"});
        });
    });
}