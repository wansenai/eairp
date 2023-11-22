#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]
///
/// Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
///
/// Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
/// with the License. A copy of the License is located at
///
/// http://opensource.wansenai.com/apache2.0/
///
/// or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
/// OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
/// and limitations under the License.
///
/// Prevents additional console window on Windows in release, DO NOT REMOVE!!

fn main() {
  tauri::Builder::default()
      .run(tauri::generate_context!())
      .expect("error while running tauri application");
}
