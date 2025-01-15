var app = angular.module('cartApp', []);

app.controller('CartController', function($scope, $http) {
        $scope.cart = {
            order:[],
            items: [],
            colors: [],
            sizes: [],
            selectedColor : null,
            selectedSize : null,
            loadColors (sanPhamId,mauSacId) {
                $scope.selectedColor = mauSacId;
                $http.get('/HienThi/' + sanPhamId + '/kichco/' + mauSacId)
                    .then(function(response) {
                        // Xử lý dữ liệu trả về từ API\
                        $scope.size = response.data;


                        console.log(response.data);
                        // Cập nhật giao diện theo dữ liệu mới nếu cần
                    })
                    .catch(function(error) {
                        console.error("Lỗi khi gọi API: ", error);
                    });
            },

            // Biến lưu trữ kích cỡ đã chọn
            selectColor(colorId) {
                $scope.selectedColor = colorId;  // Gán màu sắc đã chọn vào selectedColor
                console.log("Màu sắc đã chọn:", $scope.selectedColor);
            },

            // Xử lý khi chọn kích cỡ
            selectSize(sizeId) {
                $scope.selectedSize = sizeId;
                console.log("Kích cỡ đã chọn:", $scope.selectedSize);
            },
            // hêm sản phẩm vào giỏ hàng
            add(id) {
                console.log("màu sắc :"+ $scope.selectedColor)
                var color = $scope.selectedColor;  // Màu sắc đã chọn
                var size = $scope.selectedSize;    // Kích cỡ đã chọn
                var quantity = $scope.selectedQuantity; // Số lượng đã chọn
                if (!color || !size || !quantity) {
                    alert("Vui lòng chọn đầy đủ màu sắc, kích cỡ và số lượng!");
                    return; // Dừng hàm nếu không có đủ thông tin
                }
                console.log("ID Sản phẩm:", id);
                console.log("Màu sắc:", color);
                console.log("Kích cỡ:", size);
                console.log("Số lượng:", quantity);

                $http.get(`/HienThi/getone/${id}`, {
                    params: {
                        color: color,  // Truyền giá trị màu sắc qua query parameter
                        size: size     // Truyền giá trị kích thước qua query parameter
                    }
                }).then(function(resp) {
                    if (resp.data && resp.data.id) {
                        var item = $scope.cart.items.find(item => item.id == id && item.color == color && item.size == size);

                        if (quantity > resp.data.soLuong || quantity == 0 ) {
                            // Kiểm tra số lượng tồn kho
                            alert("Số lượng sản phẩm bạn chọn vượt quá số lượng tồn kho!");
                            console.error("Không đủ số lượng tồn kho:", resp.data.soLuongTon);
                        } else {
                            if (item) {
                                // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng
                                item.soLuong += quantity;
                            } else {
                                // Thêm sản phẩm mới vào giỏ hàng
                                resp.data.soLuong = quantity;
                                resp.data.color = color; // Gán thêm màu sắc
                                resp.data.size = size;   // Gán thêm kích thước
                                $scope.cart.items.push(resp.data);
                            }

                            alert("Thêm thành công sản phẩm vào giỏ hàng!");
                            $scope.cart.saveToLocalStorage();
                        }
                    } else {
                        console.error("Sản phẩm không tồn tại hoặc dữ liệu không hợp lệ");
                    }
                }).catch(function(error) {
                    console.error("Lỗi khi lấy sản phẩm:", error);
                });
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

            remove(id){
                var index = this.items.findIndex(item => item.id == id);
                this.items.splice(index,1);
                this.saveToLocalStorage();

            },
            calculateTotal: function() {
                return this.items.reduce((total, item) => {
                    return total + (item.soLuong * item.gia);
                }, 0);
            },


            dathang() {
                var token = localStorage.getItem("token");
                if (token) {
                    if (this.isTokenExpired(token)) {
                        alert("Token đã hết hạn, vui lòng đăng nhập lại!");
                        window.location.href = "/dang-nhap/hien-thi";
                        return;
                    }
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
            },
            isTokenExpired(token) {
                const payload = this.parseJwt(token);
                const expirationDate = payload.exp * 1000; // Chuyển từ giây sang miligiây
                return expirationDate < Date.now();
            },
            // Hàm để giải mã JWT
            parseJwt(token) {
                try {
                    const base64Url = token.split('.')[1]; // Lấy phần payload
                    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Chuẩn hóa Base64
                    const jsonPayload = decodeURIComponent(
                        atob(base64)
                            .split('')
                            .map(function (c) {
                                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                            })
                            .join('')
                    );
                    return JSON.parse(jsonPayload); // Trả về đối tượng JSON
                } catch (e) {
                    console.error("Token không hợp lệ:", e);
                    return null;
                }
            },

            addToOrder() {
                var token = localStorage.getItem("token");
                if (!token) {
                    alert("Bạn cần đăng nhập để đặt hàng!");
                    window.location.href = "/dang-nhap/hien-thi";
                    return;
                }

                // Giải mã token để lấy thông tin người dùng
                const userInfo = this.parseJwt(token);
                if (!userInfo) {
                    alert("Không thể giải mã token!");
                    localStorage.removeItem("token"); // Xóa token nếu không giải mã được
                    window.location.href = "/dang-nhap/hien-thi";
                    return;
                }

                // Kiểm tra nếu token đã hết hạn
                const now = Math.floor(Date.now() / 1000); // Thời gian hiện tại (giây)
                if (userInfo.exp && userInfo.exp < now) {
                    alert("Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!");
                    localStorage.removeItem("token"); // Xóa token khi hết hạn
                    window.location.href = "/dang-nhap/hien-thi";
                    return;
                }

                const userId = userInfo.userId; // Lấy userId từ payload
                if (!userId) {
                    alert("Không thể xác định ID người dùng từ token!");
                    localStorage.removeItem("token"); // Xóa token nếu không có userId
                    window.location.href = "/dang-nhap/hien-thi";
                    return;
                }

                if (this.items.length === 0) {
                    alert("Giỏ hàng trống!");
                    return;
                }
                if (!$scope.hoTen ) {
                    alert("Vui lòng xác nhận lại họ tên!");
                    return;
                }
                if (!$scope.soDienThoai) {
                    alert("Vui lòng xác nhận lại số điện thoại!");
                    return;
                }
                if (!$scope.email ) {
                    alert("Vui lòng xác nhận lại email!");
                    return;
                }
                if (!$scope.diaChi) {
                    alert("Vui lòng xác nhận lại địa chỉ!");
                    return;
                }
                // Dữ liệu đơn hàng
                var orderData = {
                    nguoiDungId: Number(userId), // ID người dùng từ token
                    tongTien: $scope.cart.amount,
                    hoTen: $scope.hoTen,
                    soDienThoai: $scope.soDienThoai,
                    email: $scope.email,
                    diaChi: $scope.diaChi,
                    phuongThucThanhToan: "Chuyển Khoản",
                    sanPhamList: $scope.cart.items.map(item => ({
                        sanPhamChiTietId: item.id, // ID sản phẩm chi tiết
                        soLuong: item.soLuong,
                        sanPham: item.sanPhamId,
                        giaDonVi: item.gia
                    }))
                };
                // Kiểm tra dữ liệu đơn hàng trước khi gửi
                if (!orderData.sanPhamList || orderData.sanPhamList.length === 0) {
                    alert("Không có sản phẩm trong đơn hàng.");
                    return;
                }

                console.log("Dữ liệu gửi lên server:", JSON.stringify(orderData, null, 2));

                // Gửi yêu cầu đặt hàng
                $http.post("/HienThiDonHang/dat-hang", orderData, {
                    headers: {
                        "Authorization": "Bearer " + token,
                        "Accept": "application/json" // Đảm bảo client yêu cầu JSON
                    }
                }).then(function (response) {
                    alert(response.data.message); // Thay thế cho chuỗi đơn giản
                    $scope.cart.items = [];
                    $scope.cart.saveToLocalStorage();
                }).catch(function (error) {
                    console.error("Lỗi khi đặt hàng:", error);
                    alert("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
                });
            },

        };

        // Tải giỏ hàng từ localStorage khi trang được tải lại
        $scope.cart.loadFromLocalStorage();
        // Tải màu sắc và kích cỡ từ API khi trang được tải

    }
);
