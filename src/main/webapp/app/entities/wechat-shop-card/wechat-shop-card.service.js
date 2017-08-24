(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('WechatShopCard', WechatShopCard);

    WechatShopCard.$inject = ['$resource', 'DateUtils'];

    function WechatShopCard ($resource, DateUtils) {
        var resourceUrl =  'api/wechat-shop-cards/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.ctTime = DateUtils.convertDateTimeFromServer(data.ctTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
