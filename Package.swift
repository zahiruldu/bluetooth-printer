// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "BluetoothPrinter",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "BluetoothPrinter",
            targets: ["BluetoothPrinterPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "BluetoothPrinterPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/BluetoothPrinterPlugin"),
        .testTarget(
            name: "BluetoothPrinterPluginTests",
            dependencies: ["BluetoothPrinterPlugin"],
            path: "ios/Tests/BluetoothPrinterPluginTests")
    ]
)