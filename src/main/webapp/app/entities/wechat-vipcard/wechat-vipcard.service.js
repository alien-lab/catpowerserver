(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('WechatVipcard', WechatVipcard);

    WechatVipcard.$inject = ['$resource', 'DateUtils'];

    function WechatVipcard ($resource, DateUtils) {
        var resourceUrl =  'api/wechat-vipcards/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.getTime = DateUtils.convertDateTimeFromServer(data.getTime);
                        data.activeTime = DateUtils.convertDateTimeFromServer(data.activeTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
