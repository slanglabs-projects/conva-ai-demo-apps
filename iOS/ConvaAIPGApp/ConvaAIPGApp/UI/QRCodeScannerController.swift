//
//  Copyright (c) 2017-2024 Slang Labs Private Limited. All rights reserved.
//

import UIKit
import AVFoundation

class QRCodeScannerController: UIViewController, AVCaptureMetadataOutputObjectsDelegate {
    private var captureSession: AVCaptureSession?
    private var videoPreviewLayer: AVCaptureVideoPreviewLayer?
    private var onScan: ((AssistantData?) -> Void)?
    
    init(onScan: @escaping (AssistantData?) -> Void) {
        self.onScan = onScan
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        startScanning()
    }
    
    func startScanning() {
        DispatchQueue.global(qos: .userInitiated).async { [weak self] in
            guard let self = self else { return }
            
            self.captureSession = AVCaptureSession()
            
            guard let captureDevice = AVCaptureDevice.default(for: .video) else {
                print("Failed to get the camera device")
                return
            }
            
            do {
                let input = try AVCaptureDeviceInput(device: captureDevice)
                if self.captureSession?.canAddInput(input) == true {
                    self.captureSession?.addInput(input)
                } else {
                    print("Could not add video device input to the session")
                    return
                }
            } catch {
                print("Could not create video device input: \(error)")
                return
            }
            
            // Add output to the capture session
            let captureMetadataOutput = AVCaptureMetadataOutput()
            if self.captureSession?.canAddOutput(captureMetadataOutput) == true {
                self.captureSession?.addOutput(captureMetadataOutput)
                
                captureMetadataOutput.setMetadataObjectsDelegate(self, queue: DispatchQueue.main)
                captureMetadataOutput.metadataObjectTypes = [.qr]
            } else {
                print("Could not add metadata output to the session")
                return
            }
            
            DispatchQueue.main.async {
                self.videoPreviewLayer = AVCaptureVideoPreviewLayer(session: self.captureSession!)
                self.videoPreviewLayer?.videoGravity = .resizeAspectFill
                self.videoPreviewLayer?.frame = self.view.bounds
                self.view.layer.addSublayer(self.videoPreviewLayer!)
            }
            self.captureSession?.startRunning()
        }
    }
    
    func stopScanning() {
        captureSession?.stopRunning()
        videoPreviewLayer?.removeFromSuperlayer()
    }
    
    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
        if metadataObjects.count == 0 {
            return
        }
        
        guard let metadataObject = metadataObjects.first as? AVMetadataMachineReadableCodeObject else {
            return
        }
        
        if metadataObject.type == .qr, let qrCodeValue = metadataObject.stringValue {
            stopScanning()
            
            let assistantData = parseQRCodeValue(qrCodeValue)
            
            onScan?(assistantData)
        }
    }
    
    private func parseQRCodeValue(_ qrCodeValue: String) -> AssistantData? {
        if let jsonData = qrCodeValue.data(using: .utf8) {
            do {
                if let json = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String: Any] {
                    return AssistantData(from: json)
                } else {
                    print("Invalid JSON format")
                    return nil
                }
            } catch {
                print("JSON Parsing Error: \(error.localizedDescription)")
                return nil
            }
        } else {
            print("Invalid QR code string")
            return nil
        }
    }
}
