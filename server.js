/* Entry point for node application */
//Env vars
const dotenv = require('dotenv').config();

//Express Framework
const express = require('express');
const routes = require('./api/routes/core_function_api');
const compression = require('compression');
const fs = require("fs");
const bodyParser = require('body-parser');


//Init express
let app = express();
app.set('port', (process.env.PORT || 5000));
app.use(compression());
app.use(bodyParser.urlencoded({ extended: false }));


app.listen(app.get('port'), function(){
    console.log("Running on: "+app.get('port')+" port");
});

//FireController
routes(app);