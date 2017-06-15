/**
 * Created by 橘 on 2017/5/31.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller("shopOpController",["$scope","$filter","shopopService","BuyCourse",function($scope,$filter,shopopService,BuyCourse){
        var vm = this;



        //今日售课
        function loadSche(date){
            shopopService.loadCoachArrangement(date,function(data,flag){
                if(!flag){
                    alert(data);
                }
                $scope.coachArrangements =data;
                console.log($scope.coachArrangements);
            });
        }
        loadSche();
        $scope.loadSche=loadSche;

        $scope.page={
            index:0,
            size:10
        };

        //上课状态
        angular.forEach($scope.coachArrangements,function (item) {

            if( item.courseState == '已结束'){
                item.status = false;
            }else{
                item.status = true;
            }
        });
        //下课
        $scope.getOutClass = function (id) {
            var index = -1;
            angular.forEach($scope.coachArrangements,function (item,key) {
                if(item.id == id ){
                    index = key;
                    item.courseState = '已结束';
                    if( item.courseState == '已结束'){
                        item.status = false;

                    }else{
                        item.status = true;
                    }
                };
            });

        };
        //取消教练排课
        $scope.removeCoachArrangement = function (id) {
            var index = -1;
            angular.forEach($scope.coachArrangements,function (item,key) {
                if(item.id == id ){
                    index = key;
                };
            });
            if(index != -1){
                $scope.coachArrangements.splice(index,1);
            }
            return $scope.coachArrangements;
        };
        //删除学员
        $scope.removeTrainee = function (id,traineeId) {
            var index = -1;
            var count = '';
            angular.forEach($scope.coachArrangements,function (item,key) {
                if(item.id == id ){
                    count = key;
                    angular.forEach(item.traineeName,function (it,k) {
                        if(it.traineeId == traineeId ){
                            index = k;
                        };
                    });
                    if(index != -1){
                        item.traineeName.splice(index,1);
                        item.traineeCount --;
                        if(item.traineeName.length == 0){
                            $scope.coachArrangements.splice(count,1);
                        }
                    }
                    return item.traineeName;
                };

            });
            return $scope.coachArrangements;
        };
        //获取当前日期
        var now = new Date();
        var month = now.getMonth()+1;
        var nowDate = now.getFullYear()+'-'+month+'-'+now.getDate();
        $scope.nowDate = nowDate;
        //时段
        $scope.today = function() {
            $scope.startDate = new Date();
            $scope.endDate = new Date();
            $scope.startArrangementCourseDate = new Date();
            $scope.endArrangementCourseDate = new Date();
        };
        $scope.today();
        $scope.clear = function() {
            $scope.startDate = null;
            $scope.endDate = null;
            $scope.startArrangementCourseDate = null;
            $scope.endArrangementCourseDate = null;
        };
        $scope.startTime = function() {
            $scope.popup1.opened = true;
        };
        $scope.endTime = function() {
            $scope.popup2.opened = true;
        };
        $scope.startArrangementCourse = function() {
            $scope.popupStartArrangementCourse.opened = true;
        };
        $scope.endArrangementCourse = function() {
            $scope.popupEndArrangementCourse.opened = true;
        };
        $scope.popup1 = {
            opened: false
        };
        $scope.popup2 = {
            opened: false
        };
        $scope.popupStartArrangementCourse = {
            opened: false
        };
        $scope.popupEndArrangementCourse = {
            opened: false
        };
        //分页
        /*$scope.totalItems = 5;
        $scope.currentPage = 1;

        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };

        $scope.pageChanged = function() {
            $log.log('Page changed to: ' + 5);
        };

        $scope.maxSize = 5;
        $scope.bigTotalItems = $scope.sellingCoursers.length;
        $scope.bigCurrentPage = 1;*/
        /**
         * 售课情况
         */
        loadAll();
        function loadAll(){
            BuyCourse.query({},onSuccess,onError);
        }
        function onSuccess(data) {
            $scope.BuyCourses = data;
            console.log($scope.BuyCourses)
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }

    }]);

    app.service("shopopService",["$http",function ($http) {
        this.loadCoachArrangement = function (date,callback) {
            $http({
                url:"api/course-schedulings/today",
                method:"GET"
            }).then(function(data){
                if(callback){
                    callback(data.data,true);
                }
            },function(error){
                if(callback){
                    callback(error,false);
                }
            })
        }
        this.loadSellingCoursers = function () {
            var sellingCoursers = [{
                traineeName:'陈乐乐',
                courseName:[{coursesName:'基础理论课程'},{coursesName:'健身健美高级课程'}],
                coachName:'教练1',
                courseCount:'2',
                money:'12000',
                time:'2017-6-13 11:00'
            },{
                traineeName:'陆丹',
                courseName:[{coursesName:'基础实践课程'}],
                coachName:'教练2',
                courseCount:'1',
                money:'5000',
                time:'2017-6-13 13:00'
            },{
                traineeName:'学员1',
                courseName:[{coursesName:'功能训练课程'}],
                coachName:'教练3',
                courseCount:'1',
                money:'5000',
                time:'2017-6-13 17:00'
            },{
                traineeName:'学员2',
                courseName:[{coursesName:'运动康复课程'}],
                coachName:'教练2',
                courseCount:'1',
                money:'5000',
                time:'2017-6-4 17:00'
            },{
                traineeName:'学员3',
                courseName:[{coursesName:'瑜伽'},{coursesName:'体操技能'}],
                coachName:'教练3',
                courseCount:'2',
                money:'12000',
                time:'2017-6-2 19:00'
            },{
                traineeName:'学员4',
                courseName:[{coursesName:'健身健美高级课程'}],
                coachName:'教练1',
                courseCount:'1',
                money:'5000',
                time:'2017-6-2 19:00'
            }];
            return sellingCoursers;
        }
    }]);
    app.service("classesTodayService",[function () {

    }]);
    app.service("CourseArrangementService",[function () {
        this.loadCourseArrangement = function () {
            var courseArrangements = [{
                arrangementDate:'2017-6-5',
                coachName:'教练1',
                courseName:'基础理论课程',
                startTime:'2017-6-5 17:00',
                endTime:'2017-6-5 18:30',
                attendanceNumber:'5'
            },{
                arrangementDate:'2017-6-6',
                coachName:'教练2',
                courseName:'功能训练课程  ',
                startTime:'2017-6-6 18:00',
                endTime:'2017-6-6 19:30',
                attendanceNumber:'5'
            }];
            return courseArrangements;
        }
    }]);




})();




(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider
            .state('shopop', {
            parent: 'app',
            url: '/',
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
            .state('shopop.new', {
            parent: 'shopop',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/shop/addTodayCourse.html',
                    controller: 'addCourseController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        addArrangement: function () {
                            return {
                                coachName: null,
                                courseName: null,
                                courseState: null,
                                time: null,
                                traineeCount: null,
                                traineeName: null,
                                id:null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shopop', null, { reload: 'shopop' });
                }, function() {
                    $state.go('shopop');
                });
            }]

        })
    }]);
})();

