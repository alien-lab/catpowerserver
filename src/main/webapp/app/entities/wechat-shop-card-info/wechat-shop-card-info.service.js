(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('WechatShopCardInfo', WechatShopCardInfo);

    WechatShopCardInfo.$inject = ['$resource', 'DateUtils'];

    function WechatShopCardInfo ($resource, DateUtils) {
        var resourceUrl =  'api/wechat-shop-card-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.getTime = DateUtils.convertDateTimeFromServer(data.getTime);
                        data.rechargeTime = DateUtils.convertDateTimeFromServer(data.rechargeTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
