/**
 * Created by asus on 2017/7/22.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('coachWorkScheController',['$scope','cal','coachScheService','Coach','$filter','$window','$state',function ($scope,cal,coachScheService,Coach,$filter,$window,$state) {

        $scope.coacheListSche = [];
        $scope.nowToday = null;
        $scope.nowMonthOrder = null;
        $scope.todayListSche = null;
        $scope.coaches = null;
        $scope.nowDay=null;
        $scope.nowmonth = null;
        $scope.workScheList = null;
        $scope.workSches = [];
        $scope.borderStyle = null;
        $scope.addScheTime = null;

        //每月第一天与最后一天
        $scope.dayFirst = null;
        $scope.dayLast = null;
        $scope.yearMothDayFirst = null;
        $scope.yearMothDaylast = null;
        //添加教练排班
        $scope.closePage = false;
        $scope.addScheTime = null;
        $scope.addScheWeekData = null;
        $scope.addcoachName = null;
        $scope.addcoachId = null;



        //  6*7日历
        var months=['一','二','三','四','五','六','七','八','九','十','十一','十二'];
        var weeks =['周日','周一','周二','周三','周四','周五','周六'];
        $scope.weeks = weeks;
        // 年月日初始化
        var date=new Date();
        console.log(date);
        //当前为几号
        $scope.day=date.getDate();
        $scope.nowDay =  $scope.day;
        console.log($scope.nowDay);
        //  获取当前年
        $scope.year=date.getFullYear();
        cal.setYear($scope.year);
        //  判断第几个月份，当前为几月
        var monthOrder=date.getMonth();
        $scope.monthOrder=monthOrder;
        cal.setMonthOrder(monthOrder);
        $scope.month=months[monthOrder];
        //当前月
        $scope.nowmonth=cal.setMonthOrder(monthOrder);
        console.log($scope.nowmonth);

        //获取现在的时间年月日
        //$scope.nowTime = $scope.year + '-'+ nowMonthOrder + '-'+$scope.day;

        // 获取某年的某月有几天
        var haveDaysOfOneMonth=cal.getDaysOfOneMonth($scope.year,monthOrder);
        $scope.nowMonthOrder = monthOrder+1;
        console.log($scope.nowMonthOrder);
        $scope.haveDays=cal.createDaysFrom1(haveDaysOfOneMonth);
        console.log("$scope.haveDays------------->"+$scope.haveDays);

        $scope.nowTime=[];
        for (var i = 0 ;i < $scope.haveDays.length;i++){
            $scope.nowTime.push({'day':$scope.year+'-'+$scope.nowMonthOrder+'-'+$scope.haveDays[i]});
        }
        console.log($scope.nowTime);

        // 获取当前周几，然后算出前面月份有几天，留空白
        var weekDayForFirstDayOfMonth=cal.getFirstDayOfMonth($scope.year,monthOrder);


        //留白部分
        $scope.daysOfBefore=cal.createDaysOfBlank(weekDayForFirstDayOfMonth);

        //获取上下月
        $scope.next=function(direction){
            if(direction=='p'){
                cal.getPrevMonth();
                comm();
                console.log('上个月');
                //获取当每年每月的第一天与最后一天
                $scope.dayFirst = '1';
                for(var i = 0 ; i < $scope.haveDays.length ; i++ ){
                    $scope.dayLast = $scope.haveDays[i];
                }
                $scope.yearMothDayFirst = $scope.year + '-' + $scope.nowMonth + '-'+ $scope.dayFirst;
                $scope.yearMothDaylast = $scope.year + '-' + $scope.nowMonth + '-'+ $scope.dayLast;
                coachScheService.loadMonthWorkSche($scope.yearMothDayFirst,$scope.yearMothDaylast,function (data) {
                    $scope.workScheList = data;
                    angular.forEach($scope.workScheList,function (item) {
                        $scope.todayDay = $filter('date')(item.workDate,'dd');
                        $scope.workSches.push({'coachWorkScheList':item.coachWorkScheList,'workDate':item.workDate,'status':false});

                    });
                    angular.forEach($scope.workSches,function (item) {
                        $scope.todayDay = $filter('date')(item.workDate,'dd');
                        if($scope.todayDay ==$scope.nowDay){
                            item.status = true;
                        }
                    });
                    console.log($scope.workSches);
                });
            }else if(direction=='n'){
                cal.getNextMonth();
                comm();
                //获取当每年每月的第一天与最后一天
                $scope.dayFirst = '1';
                for(var i = 0 ; i < $scope.haveDays.length ; i++ ){
                    $scope.dayLast = $scope.haveDays[i];
                }
                $scope.yearMothDayFirst = $scope.year + '-' + $scope.nowMonth + '-'+ $scope.dayFirst;
                $scope.yearMothDaylast = $scope.year + '-' + $scope.nowMonth + '-'+ $scope.dayLast;
                coachScheService.loadMonthWorkSche($scope.yearMothDayFirst,$scope.yearMothDaylast,function (data) {
                    $scope.workScheList = data;
                    angular.forEach($scope.workScheList,function (item) {
                        $scope.todayDay = $filter('date')(item.workDate,'dd');
                        $scope.workSches.push({'coachWorkScheList':item.coachWorkScheList,'workDate':item.workDate,'status':false});

                    });
                    angular.forEach($scope.workSches,function (item) {
                        $scope.todayDay = $filter('date')(item.workDate,'dd');
                        $scope.todayMonth = $filter('date')(item.workDate,'MM');
                        if($scope.todayDay ==$scope.nowDay && $scope.nowMonthOrder == $scope.todayMonth){
                            item.status = true;
                        }
                    });
                    console.log($scope.workSches);
                });
            }
        };

        function comm(){
            monthOrder=cal.getMonthOrder();
            $scope.nowMonth = monthOrder+1;

            $scope.month=months[monthOrder];

            $scope.year=cal.getYear();

            haveDaysOfOneMonth=cal.getDaysOfOneMonth($scope.year,monthOrder);
            $scope.haveDays=cal.createDaysFrom1(haveDaysOfOneMonth);

            weekDayForFirstDayOfMonth=cal.getFirstDayOfMonth($scope.year,monthOrder);
            $scope.daysOfBefore=cal.createDaysOfBlank(weekDayForFirstDayOfMonth);
        };

        //获取当前月的教练排班
        $scope.dayFirst = '1';
        for(var i = 0 ; i < $scope.haveDays.length ; i++ ){
            $scope.dayLast = $scope.haveDays[i];
        }
        $scope.yearMothDayFirst = $scope.year + '-' + $scope.nowMonthOrder + '-'+ $scope.dayFirst;
        $scope.yearMothDaylast = $scope.year + '-' + $scope.nowMonthOrder + '-'+ $scope.dayLast;
        coachScheService.loadMonthWorkSche($scope.yearMothDayFirst,$scope.yearMothDaylast,function (data) {
            $scope.workScheList = data;
            angular.forEach($scope.workScheList,function (item) {
                $scope.todayDay = $filter('date')(item.workDate,'dd');
                $scope.workSches.push({'coachWorkScheList':item.coachWorkScheList,'workDate':item.workDate,'status':false});

            });
            angular.forEach($scope.workSches,function (item) {
                $scope.todayDay = $filter('date')(item.workDate,'dd');
                 if($scope.todayDay ==$scope.nowDay){
                     item.status = true;
                 }
            });
            console.log($scope.workSches);
        });
        //获取今日教练排班
        $scope.todayListSche = [];
        $scope.nowToday = $scope.year + '-' + $scope.nowMonthOrder + '-' + $scope.day ;
        coachScheService.loadCoachScheList($scope.nowToday,function (data) {
            $scope.todayListSche = data;
            console.log($scope.coacheListSche);
        });
        //选择教练
        $scope.selectCoach = function (coach) {
            $scope.addcoachName = coach.coachName;
            $scope.addcoachId = coach.id;
        };
        //添加教练排班的时间
        $scope.getTheDay=function(workSche){
            $scope.selectDay = workSche;
            $scope.addScheTime = $filter('date')($scope.selectDay.workDate,'yyyy-MM-dd');
            $scope.addSwwwwwwwwwwwwcheTime = $filter('date')($scope.selectDay.workDate,'yyyy-MM-dd');
            console.log($scope.addScheTime);
            console.log($scope.addSwwwwwwwwwwwwcheTime);
            var currentDayOfDate = cal.getCurrentDayOfDate($scope.addScheTime);
            $scope.addScheWeekData = currentDayOfDate;
            console.log(currentDayOfDate);
            $scope.addScheWeek = $scope.weeks[currentDayOfDate];
            console.log($scope.addScheWeek);
            //教练排班记录
            coachScheService.loadCoachScheList($scope.addScheTime,function (data) {
                $scope.schworkCoaches = data;
                console.log($scope.schworkCoaches)
            });
            /*$scope.selectDay=theDay;
            comm();
            $scope.addScheTime=$scope.year + '-'+ $scope.nowMonth + '-'+$scope.selectDay;
            console.log("$scope.coacheListSche------>"+$scope.addScheTime);
            // 根据所给日期判断周几
            var currentDayOfDate = cal.getCurrentDayOfDate($scope.addScheTime);
            $scope.week = $scope.weeks[currentDayOfDate];
            //根据教练排班日期获取教练
            coachScheService.loadCoachScheList($scope.addScheTime,function (data) {
                $scope.coacheListSche = data;
                console.log($scope.coacheListSche);
            });*/
        };
        //删除教练排班
        $scope.deleteCoach = function (sche) {
            console.log(sche.id);
            /*coachScheService.deleteScheWork(sche.id,function () {});*/
            swal({
                    title: "您确定要删除"+sche.coach.coachName+"教练排班吗",
                    text: "",
                    type: "error",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确定",
                    closeOnConfirm: false
                },
                function(){
                    coachScheService.deleteScheWork(sche.id,function () {});
                    swal({
                        title:"删除成功",
                        text:"",
                        type:"success",
                    },function () {
                        $state.go('coach-workSche', null, { reload: true });
                    });
                });
        };
        $scope.deleteschework=false;
        $scope.deleteSche = function () {
          $scope.deleteschework=!$scope.deleteschework;
        };
        //添加教练排班
        $scope.saveCoachWork = function () {
            //插入如
            var map = {
                'time':$scope.addScheTime,
                'workWeekday':$scope.addScheWeekData,
                'coachId':$scope.addcoachId
            };
            coachScheService.saveCoachWorkSche(map,function (result,flag) {
                if(!flag){
                    //出现异常给提示
                    //alert("错误");
                }
                //正确返回的逻辑
            });
            if($scope.addScheTime != null && $scope.addcoachId != null && $scope.addScheWeekData != null){
                swal({
                        title: "成功添加教练排课",
                        text: "",
                        type: "success",
                        confirmButtonText: "确定",
                        confirmButtonColor: "#449d44",
                        closeOnConfirm: true
                    },
                    function(){
                        $state.go('coach-workSche', null, { reload: true });
                    });
            }else{
                swal("", "请填写完整教练排课的信息", "warning")
            }
        };
        console.log($scope.addScheTime);
        //获取所有的排班记录
        coachScheService.loadAllWorkSche(function (data) {
            $scope.workScheList = data;
            console.log(data);
        });
        //添加按钮
        $scope.openPage = function () {
            $scope.closePage = !$scope.closePage;
        };
        //取消教练排班
        $scope.cancelCoachWork = function () {
            swal({
                    title: "您确定要取消此次排班吗",
                    text: "",
                    type: "error",
                    confirmButtonText: "确定",
                    confirmButtonColor: "#449d44",
                    closeOnCancel: true
                },
                function(){
                    $state.go('coach-workSche', null, { reload: true });
                });
        };
        //获取所有的教练
        loadAll();
        function loadAll() {
            Coach.query({},onSuccess,onError);
            function onSuccess(data) {
                $scope.coaches = data;
                console.log($scope.coaches);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }]);
    app.factory('cal',function(){
        var cals={};
        cals.days=['31','28','31','30','31','30','31','31','30','31','30','31'];
        console.log(cals);
        var date=new Date();

        // 判断二月份有几天
        cals.getDaysOfFeb=function(y){
            if(y%4==0){
                if(y%400==0){
                //  整除400闰年
                    cals.days[1]=29;
                }else if(y%100==0){
                //  整除100平年
                    cals.days[1]=28;
                }else{
                    cals.days[1]=29;
                }
            }else{
                cals.days[1]=28;
            }
        };

        //  设定年份
        cals.setYear=function(y){
            cals.yearNow=y;
        };
        cals.getYear=function(){
            return cals.yearNow;
        };


        //  得到某个月有多少天
        cals.getDaysOfOneMonth= function(y,m){
        // 先确定2月份有多少天
            cals.getDaysOfFeb(y);
            return cals.days[m];
        };
        //  创建数组1~d
        cals.createDaysFrom1=function(d){
            var days=[];
            for(var i=1;i<=d;i++){
                days.push(i);
            }
            return days;
        };
        cals.createDaysOfBlank=function(len){
            var days=[];
            for(var i=0;i<len;i++){
                days.push(i);
            };
            return days;
        };

        // 设定当前是第几个月
        cals.setMonthOrder=function(n){
            cals.thisMonth=n;
        };
        // 获得当前是第几个月
        cals.getMonthOrder=function(){
            return cals.thisMonth;
        };
        // 获取下一个月，是第几个月；>12增一年，月份序号清零，<0减一年，月份序号置满11
        cals.getNextMonth=function(){
        //
            cals.thisMonth+=1;
            if(cals.thisMonth>11){
                cals.thisMonth=0;
                cals.yearNow=parseInt(cals.yearNow)+1;
            }
        };
        cals.getPrevMonth=function(){
            cals.thisMonth-=1;
            if(cals.thisMonth<0){
                cals.thisMonth=11;
                cals.yearNow=parseInt(cals.yearNow)-1;
            }
        };

        //   判断当月第一天是周几
        cals.getFirstDayOfMonth=function(y,m){
            var  dateForFirstDay=new Date(y,m,'1');
            return dateForFirstDay.getDay();
        };
        // 根据所给日期判断周几
        cals.getCurrentDayOfDate=function(y){
            var  currentDayOfDate=new Date(y);
            return currentDayOfDate.getDay();
        };
        return cals;
    });
    app.factory("coachScheResource",["$resource",function ($resource) {
        var resourceUrl = "api/coachworksche/workDate";
        return $resource(resourceUrl,{},{
            'getCoahSche':{method: 'GET',isArray:true},
            'getAllWorkSche':{url:'api/coachworksches',method: 'GET',isArray:true},
            'getMonthWorkSche':{url:'api/courseworksche/coach',method: 'GET',isArray:true},
            'delete':{url:'api/courseworksche/id',method:'DELETE'}
        });
    }]);
    app.service("coachScheService",["coachScheResource","$http",function (coachScheResource,$http) {
        //获取全部
        this.loadAllWorkSche=function(callback){
            coachScheResource.getAllWorkSche({
            },function(data){
                if(callback){
                    callback(data,true);
                }
            },function(error){
                console.log("coachScheResource.getAllWorkSche()",error);
                if(callback){
                    callback(error,false);
                }
            })
        };
        //根据排班日期获取
        this.loadCoachScheList=function(workDate,callback){
            coachScheResource.getCoahSche({
                workDate:workDate
            },function(data){
                if(callback){
                    callback(data,true);
                }
            },function(error){
                console.log("coachScheResource.getAllCoahSche()",error);
                if(callback){
                    callback(error,false);
                }
            })
        };
        //添加教练排班
        this.saveCoachWorkSche = function (param,callback) {
            $http({
                method:'POST',
                url:'api/courseworksche/info',
                data:param
            }).then(function (data) {
                if (callback){
                    callback(data.data,true);
                }
            },function(data){
                if (callback){
                    console.log("saveCoachWorkSche()"+data.data);
                    callback(data.data,false);
                }
            })
        };
        //获取每个月的教练排班
        this.loadMonthWorkSche = function (firstTime,finalTime,callback) {
            coachScheResource.getMonthWorkSche({
                'firstTime':firstTime,
                'finalTime':finalTime
            },function(data){
                if(callback){
                    callback(data,true);
                }
            },function(error){
                console.log("coachScheResource.getMonthWorkSche()",error);
                if(callback){
                    callback(error,false);
                }
            });
        };
        //删除教练排班记录
        this.deleteScheWork = function (id,callback) {
            coachScheResource.delete({
                id:id
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("coachScheResource.delete()",error);
                if(callback){
                    callback(error,false);
                }
            });
        }

    }]);

})();
