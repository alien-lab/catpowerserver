(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('WechatOrder', WechatOrder);

    WechatOrder.$inject = ['$resource', 'DateUtils'];

    function WechatOrder ($resource, DateUtils) {
        var resourceUrl =  'api/wechat-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.orderTime = DateUtils.convertDateTimeFromServer(data.orderTime);
                        data.payTime = DateUtils.convertDateTimeFromServer(data.payTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
