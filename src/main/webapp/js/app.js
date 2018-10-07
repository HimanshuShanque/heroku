var app = angular.module('myApp', ["ngRoute"]);

app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "pages/login.html",
        controller :"loginCtrl"
    })
    .when("/User", {
        templateUrl : "pages/Home.html",
        controller :"homeCtrl"
    });
});



app.controller('loginCtrl', function($scope,$http,$rootScope,$timeout) {
    $scope.initLoginPage=function(){
    	$scope.user={};
    	$scope.user1={};
    	$rootScope.loginPageToShow=true;
    	$rootScope.registerPageToShow=false;
    };
    
    $scope.SignUp=function(){
    	$scope.user={};
    	$scope.user1={};
    	$rootScope.loginPageToShow=false;
    	$rootScope.registerPageToShow=true;
    };
    
	$scope.login=function(user){
    	$http.post('home/getLoginDetails',user).then(function(bdata) {
			
    		$scope.masterDto=bdata.data;
    		if($scope.masterDto.errorMsg=="Successfully Logged in"){
    			window.location="#!User";
    		}
    		console.log($scope.masterDto);
		});
    };
    $scope.signUpUser=function(user1){
    	
    	
    		$http.post('home/createUser',user1).then(function(bdata) {
			
    		$scope.masterDto=bdata.data;
    		$scope.masterDto.errorMsg.replace('\n', '<br>');
    		if($scope.masterDto.errorMsg=="Successfully signed up"){
    			$scope.masterDto.errorMsg="Successfully signed up !You will be redirected to login page soon";
    			$timeout( function(){ $scope.initLoginPage();$scope.masterDto={};}, 3000);
    			
    		}
    		console.log($scope.masterDto);
		});
    }
});