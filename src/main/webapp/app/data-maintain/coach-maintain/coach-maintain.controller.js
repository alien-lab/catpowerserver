/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('coachMaintainController',['$scope','coachMaintainService',function ($scope,coachMaintainService) {
        $scope.coaches = coachMaintainService.loadCoach();

    }]);
    app.service('coachMaintainService',[function () {
        this.loadCoach = function () {
            var coaches = [{
                coachId:'111',
                coachName:'教练1',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://wx.qlogo.cn/mmopen/V0mhkIwf3EHrTztoocDbsZu1F8cRvcOGn2PPtywwgEL21Y7EmsXrBWlqxY0fIDKFwSBnmaR53DgSvCACswEQBw/0',
                favourableComment:'认真的为学员服务啊，对每个不同学员的不同情况有不同的方法，而且为人和蔼可亲个，深受学员的喜爱',
                negativeComment:'暂无',
                professionalLevel:'',
                serviceAttitude:'',
                likesIndex:'',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'111',
                coachName:'教练1',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://wx.qlogo.cn/mmopen/V0mhkIwf3EHrTztoocDbsZu1F8cRvcOGn2PPtywwgEL21Y7EmsXrBWlqxY0fIDKFwSBnmaR53DgSvCACswEQBw/0',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'111',
                coachName:'教练1',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://wx.qlogo.cn/mmopen/V0mhkIwf3EHrTztoocDbsZu1F8cRvcOGn2PPtywwgEL21Y7EmsXrBWlqxY0fIDKFwSBnmaR53DgSvCACswEQBw/0',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'111',
                coachName:'教练1',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://wx.qlogo.cn/mmopen/V0mhkIwf3EHrTztoocDbsZu1F8cRvcOGn2PPtywwgEL21Y7EmsXrBWlqxY0fIDKFwSBnmaR53DgSvCACswEQBw/0',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'111',
                coachName:'教练1',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://wx.qlogo.cn/mmopen/V0mhkIwf3EHrTztoocDbsZu1F8cRvcOGn2PPtywwgEL21Y7EmsXrBWlqxY0fIDKFwSBnmaR53DgSvCACswEQBw/0',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'111',
                coachName:'教练1',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://wx.qlogo.cn/mmopen/V0mhkIwf3EHrTztoocDbsZu1F8cRvcOGn2PPtywwgEL21Y7EmsXrBWlqxY0fIDKFwSBnmaR53DgSvCACswEQBw/0',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            }];
            return coaches;
        }

    }]);
})();
