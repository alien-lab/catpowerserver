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
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }]);
    app.service('coachMaintainService',[function () {
        this.loadCoach = function () {
            var coaches = [{
                coachId:'111',
                coachName:'教练1',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://mpic.tiankong.com/1ae/e65/1aee65ea1f1c340cac48b13860d60c6b/640.jpg@360h',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'2222',
                coachName:'教练2',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://mpic.tiankong.com/60b/b50/60bb50b0ea009c968807333be5ca5980/640.jpg@360h',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'333',
                coachName:'教练3',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://mpic.tiankong.com/dba/e6a/dbae6a7db98dd6dee1a24713faa1c347/640.jpg@360h',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'444',
                coachName:'教练4',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://mpic.tiankong.com/514/c58/514c589b4ac755c9be987e69775c6c1d/640.jpg@360h',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            },{
                coachId:'555',
                coachName:'教练5',
                coachPhone:'123456789',
                coachDescription:'高级教练',
                coachPicture:'http://mpic.tiankong.com/987/732/98773272c79731965b1c008b5576d39e/1H-16813.jpg@360h',
                wechatOpenId:'',
                wechatName:'',
                wechatPicture:''
            }];
            return coaches;
        }

    }]);
})();
