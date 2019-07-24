# Location

## Geocoding
- Geocoding là quá trình chuyển đổi địa chỉ (như "1600 Amphitheatre Parkway, Mountain View, CA") thành tọa độ địa lý (như latitude 37.423021 and longitude -122.083739) mà có thể sử dụng đánh dấu (place markers) trên map hoặc định vị trên map.
- Reverse geocoding ngược lại với geocoding là quá trình chuyển đổi tọa độ địa lý thành địa chỉ.
- Lớp Geocoder yêu cầu một backend service ngoài Android Framework. Các method truy vấn sẽ trả về một list rỗng nếu không có backend service trong platform.5
### Public Contructors
<img src="images/geocoding_contructor.png"/>

### Public Methods
1. getFromLocation(latitude: Double, longitude: Double, maxResult: Int): Trả về một mảng Address để mô tả địa khu vực xunh quanh latitude và longitude đã cho.
	+ Code:

	<img src="images/get_from_location.png"/>

	+ Kết quả:

	<img src="images/result_get_from_location.png"/>

2. getFromLocationName(locationName: String, maxResult: Int): Trả về một mảng Addresses để mô tả locationName.
	
	+ Code:

	<img src="images/code_location_name.png"/>

	+ Kết quả:
		
	<img src="images/result_location_name.png"/>

3. getFromLocationName(locationName: String, maxResult: Int, lowerLeftLatitude: Double, lowerLeftLongitude: Double, upperRightLatitude: Double, upperRightLongitude: Double): Trả về một mảng Address để mô tả locationName. Có thể chỉ định hộp giới hạn cho kết quả tìm kiếm bằng cách giới hạn trên và giới hạn dưới Latitude, Longitude.

	<img src="images/des_params.png"/>

	+ Code:

	<img src="images/code_location_name_1.png"/>

	+ Kết quả:
	
	<img src="images/result_location_name_1.png"/>

4. isPresent: Trả về true nếu các method getFromLocation và getFromLocationName được implement. Không có Internet các method này có thể trả về danh sách rỗng.

## Android Location Services
- Sử dụng Location Services được cung cấp bởi package *android.location*. Sử dụng các ứng dụng này có thể truy cập các dịch vụ định vị của hệ thống và cập nhật định kỳ vị trí địa lý của thiết bị.
### Location
- Lớp này đại diện cho một geographic location (vị trí địa lý), bao gồm latitude, longitudem, timestamp và các thông tin khác như bearing, altitude, velocity.
- Tất cả các location được tạo ra bởi LocationManager luôn có latitude, longitude và timestamp. Các params khác là tùy chọn.

- Tham khảo: https://developer.android.com/reference/android/location/Location

### LocationManager
- Class cung cấp quyền truy cập vào hệ thống location services. Các service này cho phép các ứng dụng có được vị trí địa lý mới nhất hoặc kích hoạt một ứng dụng được chỉ định bởi Intent khi device đi vào vị trí gần một vị trí địa lý nhất định.
- Sử dụng LocationManager ứng dụng có thể truy cập service định vị trên Android.
- Tham khảo: https://developer.android.com/reference/android/location/LocationManager
s
### LocationProvider
- Một abstract superclass cho location providers. Cung cấp định kỳ về vị trí địa lý của device.
- Có 3 loại location providers trong Android:
	+ gps(GPS, AGPS): Nhà cung cấp này xác định vị trí bằng cách sử dụng các vệ tinh. Tùy vào điều kiện, nhà cung cấp này có thể mất một lúc để trả về vị trí. Yêu cầu quyền ERIC.ACCESS_FINE_LOCATION.
	+ network (AGPS, CellID, Wifi MACID): Nhà cung cấp này xác định vị trí dựa trên tính khả dụng của các điểm truy cập Wifi và tháp di động. Kết quả được lấy bằng phương pháp tra cứu mạng. Yêu cầu một trong hai quyền ERIC.ACCESS_COARSE_LOCATION hoặc ERIC.ACCESS_FINE_LOCATION.
	+ passive (CellID, Wifi MACID): Không tự động cập nhật vị trí thay vào đó nó sử dụng kết quả được tạo bởi nhà cung cấp khác khi các ứng dụng khác yêu cầu. Yêu cầu quyền ACCESS_FINE_LOCATION, mặc dù nếu GPS không được bật, provider này chỉ có thể trả về các bản sửa lỗi thô.

	<img src="images/location_provider.png"/>

- Để lựa chọn LocationProvider tùy theo bài toán, sử dụng class *Criteria* - Lớp này chỉ ra các tiêu chí để chọn một LocationProvider như độ chính xác, sử dụng năng lượng, độ cao, tốc độ,...
- Ví dụ:

<img src="images/select_provider_cri.png"/>

	+ Criteria.ACCURACY_FINE: Yêu cầu độ chính xác cao (ACCURACY_COARSE, ACCURACY_FINE, ACCURACY_LOW, ACCURACY_MEDIUM, NO_REQUIREMENT)
	+ isAltitudeRequired = false: Nhà cung cấp trả về/không thông tin về độ cao.
	+ isBearingRequired = false: Nhà cung cấp trả về/không thông tin về bearing.
	+ isCostAllowed = true: Nhà cung cấp có được phép chịu chi phí tiền tệ hay không.
	+ powerRequirement = Criteria.POWER_HIGH: Yêu cầu năng lượng cao (POWER_LOW, POWER_MEDIUM)

- Cách tốt nhất để xử lý GPS là trước tiên sử dụng "network" or "passive" và sau đó là "gps". Tùy thuộc vào nhu cầu để chuyển đổi giữa các nhà cung cấp này bất cứ lúc nào.

- Tham khảo:
	+ https://developer.android.com/reference/android/location/LocationProvider
	+ https://developer.android.com/reference/android/location/Criteria#ACCURACY_COARSE

### Thực hành
- Lấy vị trí hiện tại của người dùng.

## Google Play Service Location APIs
- Cung cấp vị trí cho người dùng trong ứng dụng Android là rất phổ biến. Theo mặc định, Android SDK có cung cấp Location API, tuy nhiên API này không thực sự tối ưu hóa để tiết kiệm pin cho device. Vì vậy, Google đã tạo ra Fused Location Provider API tích hợp trong Google Play Services.
- Fused Location Provider quản lý các công nghệ vị trí cơ bản như GPS và Wifi  kết hợp thông minh các tín hiệu khác nhau để cung cấp thông tin vị trí theo nhu cầu của bạn.
- Để hiểu thêm về Fused Location Provider API, bạn hãy tham khảo tại đây: https://developers.google.com/location-context/fused-location-provider/?source=post_page---------------------------

### Ưu điểm
- Location API đã có sẵn khi các version Android OS đầu tiên xuất hiện, các lệnh đơn giản và thuật toán theo dõi vị trí đơn giản. Nhưng có 2 vấn đề lớn với cách tiếp cận này:
	+ Trong trường hợp bạn cần xác định vị trí chính xác, bạn phải chuyển đổi giữa các nhà cung cấp vị trí mạng và GPS (GPS không hoạt động trong nhà).
	+ Cảnh báo đã được sử dụng để thông báo tới người dùng về khoảng cách gần địa điểm và điều này gây tốn pin.
- Fused Location Provider phân tích dữ liệu GPS, mạng di động và Wifi để cung cấp dữ liệu chính xác cao nhất. Ngoài ra nó còn sử dụng các cảm biến khác nhau để xác định xem người dùng đang đi bộ, lái xe,...
- Fused Location Provider có thể sử dụng để nhận các bản cập nhật vị trí theo định kỳ. Nó có thể báo cho người dùng đang vào/ra khỏi một khu vực (tính năng geofenced: https://developer.android.com/training/location/geofencing.html)
- 
### Last Known Location
- Sử dụng Fused Location Provider, có thể yêu cầu vị trí đã biết cuối cùng của thiết bị người dùng. Trong hầu hết các trường hợp, bạn muốn lấy vị trí hiện tại của người dùng, thường tương đương với vị trí được biết đến cuối cùng của thiết bị.
- Từ Android 8.0 (API 26) trở lên, nếu ứng dụng đang chạy ở background khi yêu cầu vị trí hiện tại, thì thiết bị chỉ tính toán vị trí một vài lần mỗi giờ.

### Location Settings
- Khi app cần request location hoặc nhận permission update, thiết bị cần bật các cài đặt hệ thống phù hợp chẳng hạn như quét GPS hoặc Wifi. Thay vì trực tiếp kích hoạt các dịch vụ như GPS, ứng dụng của bạn chỉ cần chỉ định mức độ chính xác, mức tiêu thụ năng lượng cần thiết và khoảng thời gian cập nhật mong muốn, thiết bị sẽ tự động thực hiện các thay đổi phù hợp với cài đặt hệ thống. Các cài đặt này được xác định bởi đối tượng LocationRequest.
- Sử dụng đối tượng LocationRequest để lưu trữ các parameters cho các request. Các params này xác định độ chính xác cho các location request.
	+ setInterval(): Method này để thiết lập thời gian tính bằng miliseconds mà ứng dụng muốn cập nhật vị trí. Thời gian cập nhật vị trí có thể nhanh hoặc chậm hơn thời gian này hoặc có thể không có bản cập nhật nào cả.
	+ setFastestInterval(): Method này thiết lập thời gian nhanh nhất tính bằng miliseconds mà ứng dụng có thể xử lý các cập nhật vị trí. Cần cài đặt thời gian này vì các ứng dụng khác cũng ảnh hưởng đến tốc độ gửi cập nhật. Google Play services location APIs gửi các bản cập nhật với tốc độ nhanh nhất mà bất kỳ ứng dụng nào đã yêu cầu với setInterval(). Nếu tốc độ này nhanh hơn tốc độ ứng dụng có thể xử lý, bạn có thể gặp phải sự cố giao diện người dùng. Để ngăn chặn điều này, sử dụng setFastestInterval() để đặt giới hạn trên cho tốc độ cập nhật.
	+ setPriority(): Method để thiết lập mức độ ưu tiên của request: PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_HIGH_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER

- Khi bạn đã kết nối với Google Play Service và Location Services API, bạn có thể nhận current location setting của user device. Để làm điều đó, tạo LocationSettingsRequest.Builder.
- Để xác định xem các location setting có phù hợp với location request hay không, thêm OnFailureListener vào Task để xác thực location setting. Sau đó, kiểm tra xem đối tượng Exception có phải là instance của ResolvableApiException không. Sau đó, hiển thị hộp thoại nhắc người dùng cho phép sửa đổi location setting bằng cách gọi startResolutionForResult().

### Location Updates
- Nếu ứng dụng của bạn liên tục theo dõi vị trí, nó có thể cung cấp thông tin phù hợp hơn tới người dùng. Ngoài cách nhận vị trí cuối của thiết bị, một cách tiếp cận trực tiếp hơn là yêu cầu cập nhật định kỳ từ Fused Location Provider dựa trên Wifi và GPS.

## Tài liệu tham khảo
- Geocoding: https://developer.android.com/reference/android/location/Geocoder
- Location: https://developer.android.com/reference/android/location/Location
- LocationManager: https://developer.android.com/reference/android/location/LocationManager
- LocationProvider:
	+ https://developerlife.com/2010/10/20/gps/
	+ https://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-on-a/3145655#3145655
	+ https://medium.com/@maheshikapiumi/android-location-services-7894cea13878
- Fused Location Provider API: 
	+ https://developer.android.com/training/location/change-location-settings
	+ https://developer.android.com/training/location/receive-location-updates
	+ https://lembergsolutions.com/blog/fused-location-provider

