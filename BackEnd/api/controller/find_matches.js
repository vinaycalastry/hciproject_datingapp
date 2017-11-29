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
        
        var interests = snapshot.child('interests').val();
		console.log(interests);
		if(interests == null){
			res.status(200).json("No matches found");
		}
        matchDate(profileId, interests).then(function(matchID){
          console.log("test:"+matchID);
          res.status(200).json(matchID);  
        }).catch(function(err){
          res.status(404).json({error:err});
        });
      }, function (errorObject) {
        console.log("The read failed: " + errorObject.code);
      });
    
      
}


//Match with potential algorithm
function matchDate(profId, interestList){

  return new Promise(function(resolve, reject){
    try{
      var matchedID = [];
      var query = firebase.database().ref("Profiles/0/").orderByKey();
    query.once("value")
      .then(function(snapshot) {
        snapshot.forEach(function(childSnapshot) {
		if(matchedID !=9){
          var key = childSnapshot.key;
		  console.log(key);
		  var objarr={};
          var childref = database.ref('Profiles/0/').child(key);
		  var likes = snapshot.child(key).child('interests').val();
		
       	  var picurl = snapshot.child(key).child('picurl').val();
		  console.log(likes+" "+picurl);
		var vig=0;
		 if(likes!=null){
			for(var i=0; i<interestList.length;i++){
			  for(var j=0; j<likes.length ;j++){
				  if(interestList[i].toLowerCase()==likes[j].toLowerCase()){
						objarr[0]=key;
						objarr[1]=picurl;
						matchedID.push(objarr);
						vig=1;
				  }
				  if(vig ==1){
					  break;
				  }
				  
			  }
			    if(vig ==1){
					  break;
				  }
			  
		 }
		
		objarr={};
		 }
        //var childData = childSnapshot.val();
		}});
      console.log(matchedID);
      resolve(matchedID);
    });
        
        
    }
    catch(e){
      reject(e);
    }
  });

  
  
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
  return new Promise(function(resolve, reject){
    try{
      var check = -1;
      for( var i=0; i<src.length;i++){
        for(var j=0; j<src.length;j++){
          if(src[i]==dest[j]){
            check++;
          }
        }
      }
      resolve(check);
    }
    catch(e){
      reject(e);
    }
  });
	
}