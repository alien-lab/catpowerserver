/**
 * Created by 橘 on 2017/5/31.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller("shopOpController",["$scope","shopopService",function($scope,shopopService){
        $scope.page={
            index:0,
            size:10
        }

        function loadbuycourse(){
            shopopService.loadbuycourse($scope.page.index,$scope.page.size,function(data){

            });
        }

        $scope.loadbuycourse=loadbuycourse;
        loadbuycourse();

    }]);

    app.service("shopopService",["shopopResource",function(shopopResource){
        this.loadbuycourse=function(index,size,callback){
            shopopResource.query({
                index:index,
                size:size
            },function(data){
                console.log("shopopResource.query()",data);
                if(callback){
                    callback(data,true);
                }
            },function(error){
                console.log("shopopResource.query()",error);
                if(callback){
                    callback(error,false);
                }
            })
        }
    }]);

    app.factory("shopopResource",["$resource",function($resource){
        var resourceUrl =  'api/buy-courses/today';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET'}
        });
    }]);
})();


(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider.state('shopop', {
            parent: 'app',
            url: '/shopop',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/shop/shop.html',
                    controller: 'shopOpController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shop');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }]);
})();
