(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('FriendyShop', FriendyShop);

    FriendyShop.$inject = ['$resource', 'DateUtils'];

    function FriendyShop ($resource, DateUtils) {
        var resourceUrl =  'api/friendy-shops/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createTime = DateUtils.convertDateTimeFromServer(data.createTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'qr':{
                method: 'GET',
                url:'api/friendy-shops/qr/:id'
            }
        });
    }
})();
