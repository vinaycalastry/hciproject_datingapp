const findMatches = require('../controller/find_matches.js');


module.exports = function(app){
    app.get('/findmatch/:profId', findMatches.getMatches);
	app.get('/', function(req, res){res.send("Amante GetMatches Service")});
}