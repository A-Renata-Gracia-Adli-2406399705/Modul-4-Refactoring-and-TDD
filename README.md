# Modul 1

## Reflection 1

Penerapan clean code yang saya terapkan adalah meaningful names di mana saya menggunakan nama-nama variable dan function yang jelas dan bermakna. Saya juga tidak menuliskan komentar-komentar yang tidak penting. Layout dan formatting juga saya terapkan.


## Reflection 2

1. Setelah membuat unit test dan berhasil, saya merasa lebih confident dengan kode yang sudah dibuat. Tidak ada jumlah yang pasti dalam membuat unit test, tetapi paling tidak unit test yang dibuat sudah mencakup seluruh function yang ada.
   Cara tahu kalau unit test sudah cukup untuk verify programnya adalah dengan melihat code coverage. Semakin tinggi code coverage, semakin tinggi pula kemungkinan keberhasilan program.
   Walaupun code coverage sudah 100%, bukan berarti kode bebas dari bugs atau error. Bisa saja terdapat logika atau input tak terduga.


2. Menurut saya dengan menambah functional test suite baru, dapat menimbulkan bebarapa potensi masalah clean code. Misalnya prosedur setup yang sama ditulis berulang di beberapa kelas test. Selain itu, jika terjadi perubahan pada setup, maka harus diperbarui di banyak tempat.
   Dengan demikian, saya merasa bahwa hal itu dapat menurunkan kualitas kode.
   Beberapa solusi yang dapat diterapkan adalah dengan membuat base test class yang berisi setup umum dan variabel bersama, lalu mewariskannya ke kelas test lain. Selain itu, menggunakan class helper atau utility untuk fungsi yang sering digunakan.


# Modul 2

1. Saat PMD dijalankan, tidak ada code quality issue yang ditemukan. Namun, sebelumnya saya sudah melakukan refactoring pada ProductController dan ProductServiceImpl, yaitu mengubah mekanisme dependency injection dari field injection menjadi constructor injection.
   Sebelumnya, dependency diinject langsung ke field menggunakan @Autowired. Lalu, untuk meningkatkan code quality, saya ubah menjadi constructor injection agar mengikuti best practice. Dengan constructor injection, bisa meningkatkan testability karena dependency bisa diinject saat testing, meningkatkan keamanan dan konsistensi karena dependency bisa dideklarasikan sebagai final.


2. Iya, implementasi saat ini sudah memenuhi definisi Continuous Integration (CI) dan Continuous Deployment (CD).
   Continuous Integration sudah diterapkan karena setiap push dan pull request secara otomatis memicu workflow GitHub Actions yang menjalankan build, test, dan code analysis (Jacoco, PMD, OSSF Scorecard). Ini memastikan bahwa setiap perubahan code akan langsung divalidasi sebelum diintegrasi ke branch utama (main).
   Continuous Deployment juga sudah diterapkan karena setiap perubahan yang berhasil dimerge ke branch main secara otomatis dideploy ke platform PaaS (Koyeb). Lalu, seluruh prses (testing, code coverage checking, code scanning, deployment) berjalan secara otomatis melalui workflow yang terintegrasi yang merupakan cerminan dari CI/CD.

link: sunny-robby-adv-prog-ac3b0cbf.koyeb.app/


# Modul 3

1. SOLID principles yang diapply:
   
a) Single Responsibility Principle (SRP): sebuah class hanya boleh memiliki satu tanggung jawab atau hanya menangani satu fungsi tertentu.
      
Penerapan yang sudah sesuai:
- Pada model, class Product hanya menyimpan data product dan class Car hanya menyimpan data mobil saja.
- Pada repository, class ProductRepository hanya menyimpan dan mengambil data product dari storage.
- Pada service, class productServiceImpl hanya mengatur business logic untuk product dan class CarServiceImpl hanya mengatur business logic untuk car.

Kesalahan:
- CarController extends ProductController sehingga CarController membawa tanggung jawab ProductController padahal mereka menangani hal yang berbeda (satu untuk product dan satu lagi untuk car)
- Terdapat variable yang tidak digunakan pada CarRepository yang membuat class CarRepository memiliki elemen yang tidak berhubungan dengan tanggung jawabnya.

Perbaikan:
- Menghapus inheritance antara CarController dengan ProductController agar CarController tidak lagi mengextend ProductController sehingga setiap controller hanya memiliki satu tanggung jawab.
- Menghapus variable yang tidak terpakai (static int id = 0;)


b) Open Closed Principle (OCP): terbuka untuk menambah tetapi tertutup untuk modifikasi artinya memungkinkan untuk menambah fitur tanpa mengubah kode sebelumnya.

Penerapan yang sudah sesuai: Service menggunakan interface ProductService dan CarService lalu diimplementasikan oleh ProductServiceImpl dan CarServiceImpl. Kemudian, controller menggunakan interface. Jika ingin membuat service baru, controller tidak perlu diubah.

Kesalahan: class CarController extends class ProductController sehingga jika mau menambah controller baru, ProductController harus dimodifikasi.

Perbaikan: Menghapus inheritance antara CarController dengan ProductController agar CarController tidak lagi mengextend ProductController sehingga jika ingin menambah controller baru, tidak perlu mengubah kode lama.


c) Liskov Substition Principle (LSP): subclass harus bisa menggantikan superclass tanpa merusak program.

Kesalahan: class CarController extends class ProductController padahal CarController bukan jenis ProductController sehingga jika kita menulis ProductController controller = new CarController(); program bisa error karena service yang dipakai berbeda.

Perbaikan: Menghapus inheritance antara CarController dengan ProductController agar CarController tidak mengextend ProductController.


d) Interface Segregation Principle (ISP): interface besar harus dipecah menjadi interface kecil sehingga class hanya menggunakan method yang diperlukan.

Penerapan yang sudah sesuai:
- Interface ProductService digunakan seluruhnya oleh ProductServiceImpl
- Interface CarService digunakan seluruhnya oleh CarServiceImpl


e) Dependency Inversion Principle (DIP): high-level module tidak boleh bergantun pada low-level module, tetapi harus bergantung pada abstraction.

Penerapan yang sudah sesuai: ProductController bergantung pada interface ProductService, bukan pada ProductServiceImpl.

Kesalahan: CarController bergantung pada implementation yaitu CarServiceImpl

Perbaikan: Mengubah private CarServiceImpl carservice; menjadi private CarService carservice; sehingga CarController bergantung pada abstraction



2. Keuntungan menerapkan SOLID:
- Dengan SRP, tiap class memiliki tanggung jawab yang jelas sehingga kode lebih mudah dimaintain.
   Contoh: Jika ada bug pada CarRepository, cukup fix CarRepository saja tanpa menyentuh yang lain.
- Dengan OCP, kita bisa tambah fitur tanpa mengubah kode sebelumnya sehingga kode lebih mudah untuk dikembangkan.
   Contoh: Jika ingin menambah model Bike dengan BikeController, BikeService, dan BikeRepository, tidak perlu mengubah bagian product dan car.
- Dengan ISP dan juga SRP, tiap class punya fungsi yang jelas sehingga program lebih rapi dan mudah dipahami.
   Contoh: model untuk data, repository untuk data storage, controller untuk menangani request, service untuk business logic
- Dengan DIP, kode lebih mudah ditest karena bisa menggunakan mock object di unit test.
   Contoh: ProductControllerTest menggunakan mock(ProductService.class)



3. Kerugian jika tidak menerapkan SOLID:
- Jika tidak menerapkan SRP, jika satu class punya banyak tanggung jawab, maka perubahan kecil bisa menimbulkan bug di bagian lain.
   Contoh: jika ProductController juga mengatur Car, maka perubahan di Product bisa merusak Car. Selain itu, jika variable tidak terpakai di CarRepository tidak dihapus, kode akan terlihat membingungkan dan sulit dibaca.
- Jika tidak menerapkan OCP, akan sulit untuk menambah fitur baru.
   Contoh: jika CarController masih mengextends ProductController, maka setiap perubahan di ProductController bisa merusak CarController
- Jika tidak menerapkan ISP, interface akan menjadi terlalu besar dan sulit dipahami, serta perubahan kecil bisa berdampak besar, code juga menjadi tidak reusable.
   Contoh: jika ingin menambah suatu method, maka semua implementasi harus diupdate meskipun tidak membutuhkan fitur tersebut sehingga bisa menimbulkan method kosong
- Jika tidak menerapkan DIP, kode akan sulit ditest.
   Contoh: jika CarController menggunakan CarServiceImpl instead of CarService, maka unit test akan sulit untuk dibuat karena tidak bisa menggunakan mock interface.
