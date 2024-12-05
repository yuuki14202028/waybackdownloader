# WaybackDownloader

WaybackMachineからサイトのデータを一括で保存するScalaのコードです。<br>
成果物などはありませんから、ローカルなどに落として適当に実行してください。<br>
恐らく、WaybackMachineのAPIには15/60[req/sec]の呼び出し制限があるので、 ```Thread.sleep(10000)```速度を落としています。<br>

## 変数
- targetUrlはサイトのURLです。
- outputDirは保存するディレクトリです。
- matchType=prefixを消すと完全一致になります。

## 依存
- scala3.3.4
- sttp.client 3.10.1
