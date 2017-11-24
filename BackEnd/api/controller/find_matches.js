//Get reference to Firebase SDK
const firebase = require('firebase');

//Firebase Connection
const config = {
  apiKey: "AIzaSyCyz3D_8i-RsJ8TWtPC6Hd5ysNl0RkSPBc",
  authDomain: "amante-85210.firebaseapp.com",
  databaseURL: "https://amante-85210.firebaseio.com/",
  storageBucket: "gs://amante-85210.appspot.com/"
};

//Connect to firebase instance
firebase.initializeApp(config);

// Get a reference to the database service
const database = firebase.database();


//Get request getMatches handler
module.exports.getMatches = function(req, res){    
    var profileId = req.params.profId;
    var profileRef = database.ref('Profiles/0/').child(profileId);
    profileRef.on("value", function(snapshot) {
        
        var likes = snapshot.child('likes').val();
        var dislikes = snapshot.child('dislikes').val();

        var matchedPeople = matchDate(profileId, likes, dislikes);
        
        updatedMatchesRDB(profileId, matchedPeople);

      }, function (errorObject) {
        console.log("The read failed: " + errorObject.code);
      });
    
    res.status(200).json("Ended");    
}


//Match with potential algorithm
function matchDate(profId, likeList, dislikeList){
    console.log(profId);
    console.log('-----');
    console.log(likeList);
    var matchedID = ['v','i','n'];
    return matchedID;
}

//Match the updated dates in the realtime DB
function updatedMatchesRDB(profId,matchestoUpdate){
    var matchesUpdated = {};
    matchesUpdated.liked_id = matchestoUpdate;
    console.log(matchesUpdated);
    var matchesRef = database.ref('matches/0/');
    matchesRef.child(profId).set(matchesUpdated);
}