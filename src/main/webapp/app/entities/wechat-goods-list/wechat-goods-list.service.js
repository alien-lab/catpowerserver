(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('WechatGoodsList', WechatGoodsList);

    WechatGoodsList.$inject = ['$resource'];

    function WechatGoodsList ($resource) {
        var resourceUrl =  'api/wechat-goods-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
