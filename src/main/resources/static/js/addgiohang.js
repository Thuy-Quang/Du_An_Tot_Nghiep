var app = angular.module('cartApp', []);

app.controller('CartController', function($scope, $http) {
    $scope.cart = {
        items: [],
        colors: [],  // Mảng chứa thông tin màu sắc
        sizes: [],   // Mảng chứa thông tin kích cỡ

        // Hàm thêm sản phẩm vào giỏ hàng
        add(id) {
            var item = this.items.find(item => item.id == id);
            if (item) {
                // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng
                item.soLuong++;
                this.saveToLocalStorage();
            } else {
                // Gọi API để lấy thông tin sản phẩm theo ID
                $http.get(`/HienThi/getone/${id}`).then(function(resp) {
                    if (resp.data && resp.data.id) {
                        resp.data.soLuong = 1;
                        $scope.cart.items.push(resp.data);
                        alert("Thêm thành công sản phẩm vào giỏ hàng!");
                        $scope.cart.saveToLocalStorage();
                    } else {
                        console.error("Sản phẩm không tồn tại hoặc dữ liệu không hợp lệ");
                    }
                }).catch(function(error) {
                    console.error("Lỗi khi lấy sản phẩm:", error);
                });
            }
        },

        // Lưu giỏ hàng vào localStorage
        saveToLocalStorage: function() {
            var json = JSON.stringify(this.items);
            localStorage.setItem("cart", json);
        },

        // Lấy giỏ hàng từ localStorage khi trang được tải lại
        loadFromLocalStorage: function() {
            var storedCart = localStorage.getItem("cart");
            if (storedCart) {
                this.items = JSON.parse(storedCart);
            }
        },

        get count() {
            return this.items
                .map(item => item.soLuong)
                .reduce((total, soluong) => total += soluong, 0);
        },

        get amount() {
            return this.items
                .map(item => item.soLuong * item.gia)
                .reduce((total, soluong) => total += soluong, 0);
        },

        // Hàm lấy màu sắc từ API
        loadColors: function() {
            $http.get(`/HienThi/mau-sac`).then(function(response) {
                $scope.cart.colors = response.data; // Lưu màu sắc vào mảng
            }).catch(function(error) {
                console.error("Lỗi khi lấy màu sắc:", error);
            });
        },

        // Hàm lấy kích cỡ từ API
        loadSizes: function() {
            $http.get(`/HienThi/kich-co`).then(function(response) {
                $scope.cart.sizes = response.data; // Lưu kích cỡ vào mảng
            }).catch(function(error) {
                console.error("Lỗi khi lấy kích cỡ:", error);
            });
        },
        remove(id){
            var index = this.items.findIndex(item => item.id == id);
            this.items.splice(index,1);
            this.saveToLocalStorage();

        },
        dathang() {
        var token = localStorage.getItem("token");
        if (token) {
            $http.get("/nguoi-dung/thong-tin", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            }).then(function (response) {
                console.log("Dữ liệu nhận được:", response.data);

                var user = response.data;

                // Gán dữ liệu vào các phần tử HTML
                var hoTenElement = document.getElementById("hoTen");
                var soDienThoaiElement = document.getElementById("soDienThoai");
                var emailElement = document.getElementById("email");
                var diaChiElement = document.getElementById("diaChi");

                if (hoTenElement) hoTenElement.value = user.hoTen || "";
                if (soDienThoaiElement) soDienThoaiElement.value = user.soDienThoai || "";
                if (emailElement) emailElement.value = user.email || "";
                if (diaChiElement) diaChiElement.value = user.diaChi || "";

                // Kiểm tra URL hiện tại trước khi chuyển hướng
                if (!window.location.pathname.includes("/dat-hang")) {
                    window.location.href = "/dat-hang";
                }
            }).catch(function (error) {
                console.error("Không thể tải thông tin người dùng:", error);
            });
        } else {
            console.warn("Người dùng chưa đăng nhập.");
            window.location.href = "/dang-nhap/hien-thi";
        }
    }





};


    // Tải giỏ hàng từ localStorage khi trang được tải lại
    $scope.cart.loadFromLocalStorage();
    // Tải màu sắc và kích cỡ từ API khi trang được tải
    $scope.cart.loadColors();
    $scope.cart.loadSizes();
});
