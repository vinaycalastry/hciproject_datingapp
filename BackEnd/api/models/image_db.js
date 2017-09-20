//Require imports: Mongoose for DB, Creds for mongoose
var mongoose = require('mongoose');

var MONGO_URI = process.env.MONGO_URI;

//Replace mongoose promise with Node
mongoose.Promise = global.Promise;

//Connect to MongoDB
mongoose.connect(MONGO_URI);

//UploadDaterDetails

var imageUploadSchema = new mongoose.Schema({
    img: { data: Buffer, contentType: String }
});

module.exports = mongoose.model('ImageUpload', imageUploadSchema);