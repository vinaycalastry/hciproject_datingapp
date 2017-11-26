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

var matchedPeople=[];
//Get request getMatches handler
module.exports.getMatches = function(req, res){    
    var profileId = req.params.profId;
    var profileRef = database.ref('Profiles/0/').child(profileId);
    profileRef.on("value", function(snapshot) {
        
        var likes = snapshot.child('likes').val();
        var dislikes = snapshot.child('dislikes').val();

        matchedPeople = matchDate(profileId, likes, dislikes);
		console.log(matchedPeople);

      }, function (errorObject) {
        console.log("The read failed: " + errorObject.code);
      });
    
    res.status(200).json(matchedPeople);    
}


//Match with potential algorithm
function matchDate(profId, likeList, dislikeList){
  console.log("likelist"+likeList);
  //var matchIDs=[{}];
  var matchedID = [];
  var query = firebase.database().ref("Profiles/0/").orderByKey();
query.once("value")
  .then(function(snapshot) {
    snapshot.forEach(function(childSnapshot) {
      // key will be "ada" the first time and "alan" the second time
      var key = childSnapshot.key;
	  var objarr={};
	   var childref = database.ref('Profiles/0/').child(key);
		childref.on("value", function(snapshot) {
        var likes = snapshot.child('likes').val();
        var dislikes = snapshot.child('dislikes').val();
		 var picurl = snapshot.child('picurl').val();
		var vig=0;
		objarr={};
		if(checkEquals(likes,likeList)!=-1 && key!=profId){
			console.log("matched"+ likes)
			console.log(key);
			objarr[0]=key;
			objarr[1]=picurl;
			matchedID.push(objarr);
			console.log(objarr);
			vig=1;
		}
		if(checkEquals(dislikes,dislikeList)!=-1 && key!=profId && vig==0){
			console.log("matched"+ likes)
			console.log(key);
			objarr[0]=key;
			objarr[1]=picurl;
			console.log(objarr);
			matchedID.push(objarr);
		//	matchedID.push(key);
		}
      }, function (errorObject) {
        console.log("The read failed: " + errorObject.code);
      });
     
      var childData = childSnapshot.val();
	 // console.log(key+" "+childData);
  });
});

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

function checkEquals(src,dest){
	var check = -1;
	for( var i=0; i<src.length;i++){
		for(var j=0; j<src.length;j++){
			if(src[i]==dest[j]){
				check++;
			}
		}
	}
	return check;
}