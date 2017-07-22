/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('coachMaintainController',['$scope','Coach',function ($scope,Coach) {
        var vm = this;
        loadAll();

        function loadAll(){
            Coach.query({

            },onSuccess,onError);
            function onSuccess(data) {
                vm.coaches = data;
                console.log(vm.coaches);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }]);

})();
(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');

    app.factory('coachInfoResource',['$resource',function ($resource) {
        var resourceUrl = "api/buy-courses/courses/coachId";
        return $resource(resourceUrl,{},{
            'getCoachOtherInfo':{method:'GET',isArray:true},
            'getCoachEvaluates':{url:'api/coach-evaluates-learner/coach-evaluates/coachId',method:'GET',isArray:true}
        });
    }]);
    app.service('coachInfoService',['coachInfoResource',function (coachInfoResource) {
        this.loadCoachOtherInfo = function (coachId,callback) {
            coachInfoResource.getCoachOtherInfo({
                'coachId':coachId
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                if(callback){
                    console.log("coachInfoResource.getCoachOtherInfo" + error);
                    callback(error,false)
                }
            });
        };
        //根据教练ID获取教练的评价信息
        this.loadCoachComment = function (coachId,callback) {
            coachInfoResource.getCoachEvaluates({
                'coachId':coachId
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                if(callback){
                    console.log("coachInfoResource.getCoachEvaluates()" + error);
                    callback(error,false)
                }
            });
        }
    }]);
})();
