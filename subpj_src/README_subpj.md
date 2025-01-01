# matsu.num.Specialfunction, subpj
subpj　(ソースフォルダ `subpj_src`) は, 
`matsu.num.Specialfunction` モジュールのコーディングを補助するためのコンポーネントを扱っている.
ディストリビューションには含まれない.

メインソースで使用しないライブラリを subpj の中で要求する場合がある.
要求されたライブラリがある場合はメインソースの `module-info.java` は修正せず,
クラスパス上に配置する
(subpj は Java のモジュールシステム外である).

ライブラリのうち適切な物が存在せず, subpj が適切に動作しない場合は `subpj_src` を削除すること.

今のバージョンではおそらく次が使用されている.
(ただし, このドキュメントが十分に整備されていないかもしれない.)

- `matsu.num.Approximation`
- `matsu.num.MathType`
