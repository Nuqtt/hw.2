package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MyApp(navController = navController)
        }
    }
}
enum class MainScreen() {
    Start,
    ShowMore
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ComposableDestinationInComposeScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    navController: NavHostController = rememberNavController()
) {
    val names = listOf(
        "台北市立動物園",
        "劍潭山老地方觀機平台",
        "台北101",
        "大稻埕碼頭",
        "臺北市立兒童新樂園",
        "公館寶藏巖",
        "國立故宮博物院",
        "台北行天宮",
        "華山文創園區",
        "士林夜市"
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBackIcon = currentRoute != MainScreen.Start.name
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("台北景點推薦") },
                navigationIcon = {
                    if (showBackIcon) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "back")
                        }
                    } else {
                        IconButton(onClick = { /* Handle menu icon click */ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = MainScreen.Start.name, modifier = Modifier.padding(innerPadding)) {
            composable(route = MainScreen.Start.name) {
                StartItem(names = names) { index ->
                    navController.navigate("${MainScreen.ShowMore.name}/$index")
                }
            }
            composable(
                route = "${MainScreen.ShowMore.name}/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            ) { backStackEntry ->
                val index = backStackEntry.arguments?.getInt("index") ?: 0
                val name = names.getOrNull(index) ?: ""
                ShowMoreScreen(index = index, name = name, navigateUp = { navController.navigateUp() })
            }
        }
    }
}


@Composable
fun StartItem(
    modifier: Modifier = Modifier,
    names: List<String> = List(10) { "$it" },
    navigateToShowMore: (Int) -> Unit
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        itemsIndexed(items = names) { index, name ->
            StartScreen(name = name, index = index, navigateToShowMore = navigateToShowMore)
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    name: String,
    index: Int,
    navigateToShowMore: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 20.sp
                )
            }
            ElevatedButton(
                onClick = { navigateToShowMore(index) }
            ) {
                Text(
                    "Show More",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowMoreScreen(
    index: Int,
    name: String,
    navigateUp: () -> Unit
){
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val image = painterResource(Picture(index+1))
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = Introduce(index+1),
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ElevatedButton(
            onClick = {
                val url = "https://www.google.com/maps/search/?api=1&query=${Uri.encode("台北市 $name")}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        ) {
            Text(
                "Google Map",
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun Picture(index: Int): Int {
    return when (index) {
        1 -> R.drawable.tp1
        2 -> R.drawable._021110__2_210113_30_1024x768_jpg
        3 -> R.drawable.pic_ashx
        4 -> R.drawable._0210409103706_2a2890d4
        5 -> R.drawable.pic_ashx_2
        6 -> R.drawable._23137
        7 -> R.drawable.pic_ashx_3
        8 -> R.drawable.pic_ashx_4
        9 -> R.drawable.pic_ashx_5
        10 -> R.drawable.pic_ashx_6
        else -> R.drawable.ic_launcher_foreground
    }
}
@Composable
fun Introduce(index: Int): String{
    return when (index) {
        1 -> "園區總面積原為182公頃，隨後因捷運動物園站及北二高的開闢，移撥出部分土地，目前仍有165公頃之廣，其中已開發使用區域近百公頃，是全臺佔地面積最大的動物園。園內包含8個戶外展示區、6個室內展示館、2個環境教育教學場所，2021年底有動物354種、2,407隻（未計算昆蟲、部分魚類及農委會收容計畫動物）。整個園區被自然次生林地所圍繞，是一處結合自然景觀形成具生態特色之休閒場所。全園最大的特色是展示環境的佈置上採用「地理生態展示法」，依照動物原先的生存環境加以佈置在新的環境內，使動物脫離鐵籠的束縛，有自由的活動空間，並創造出與動物原生地最接近的生活環境，使動物不必去改變其生活習性，也讓遊客更能了解動物，是一座具有教育、研究、保護及娛樂功能的動物園。"
        2 -> "劍潭山海拔153公尺，位於台北市士林區圓山風景區，是圓山飯店後山的北稜，也是台北市最接近市中心的小山，沿著稜脊步道緩緩前行可抵達其他山岳，走起來幾乎是輕鬆自在，在交通便捷上佔有極大之優勢。登山口位於中山北路四段公車劍潭站旁，這裡曾是軍事管制區與保安林地，因此自然林相與生態植物保存的相當完整；步道兩旁許多人工栽種植被與經濟植物，讓步道充滿盎然的綠色魅力。這條步道已成為市民晨運健身的場所，如位於稜線上「老地方」，就是大家熟知的健身據點。步道沿途可以欣賞圓山飯店、基隆河河濱公園等景致，步道盡頭為大直的通北街165巷、北安路口處，是一條坡緩易行的郊山步道。"
        3 -> "TAIPEI 101座落於台北最菁華地段，是國內建築界有史以來最大的工程專案。該專案主要由國內十四家企業共同組成的台北金融大樓股份有限公司，與國內外專業團隊聯手規劃，並由國際級建築大師李祖原精心設計，超越單一量體的設計觀，以吉祥數字「八」（「發」的諧音），作為設計單元。每八層樓為一個結構單元，彼此接續、層層相疊，構築整體。在外觀上形成有節奏的律動美感，開創國際摩天大樓新風格。TAIPEI 101多節式外觀，以高科技巨型結構（Mega Structure）確保防災防風的顯著效益。每八層形成一組自主構成的空間，自然化解高層建築引起之氣流對地面造成的風場效應，透過建築設計綠化植栽區的區隔，確保行人的安全與舒適性。大樓造型宛若勁竹節節高昇、柔韌有餘，象徵生生不息的傳統建築意涵。內斜七度的建築面，層層往上遞增；無反射光害的高度透明省能隔熱帷幕玻璃，讓人們在台灣的最高建築內，觀天看地。高科技材質及創意照明，以透明、清晰、營造視覺穿透效果，與自然及週遭環境大尺度的融合。"
        4 -> "大稻埕原是平埔族的居住地，因萬華（艋舺）同安人發生激烈的械鬥，造成族人移至大稻埕定居，開始大稻埕淡水河旁商店和房屋的興建，淡水港開放後，大稻埕在劉銘傳的治理下成為臺北城最繁華的物資集散中心，以茶葉、布料為主要貿易交易，當時的延平北路及貴德街一帶便是商業活動的重心，也讓大稻埕早年的歷史多采多姿、令人回味。大稻埕碼頭位於淡水河畔，現今五號水門處（環河北路、民生西路口）。大稻埕的發展歷史，與大稻埕碼頭有著密切的關係，淡水開港通商，外商洋行紛紛到此開設商號，促成大稻埕往後的繁華。現今雖然碼頭功能不復當年，但已轉型為民眾休閒遊憩的去處。近年來大稻埕碼頭除了迎節慶放煙火之外，沿岸並闢建自行車道，民眾可以悠閒騎單車享受河畔之旅，同時欣賞大稻埕的古意景緻。"
        5 -> "臺北市立兒童新樂園，前身為位於圓山的臺北市立兒童育樂中心。在民國95年4月11日，兒童育樂中心被列入國定遺址範圍。原址受到航高、文化資產保存法、水利法等限制，開發、更新及營運受到相當大的影響及限制。為克服此一侷限，民國96年10月市府規劃於士林區興建臺北市立兒童新樂園（約5公頃）。與鄰近的臺北市立天文館、國立科學教育館、美崙公園、雙溪水域整體規劃，並將「建構臺北兒童新樂園」納入市政白皮書工作重點。期許打造一座兼具教育、休閒遊憩與文化功能的都會型親子休閒園地，提供市民戶外活動的新選擇。兒童新樂園於民國103年完工後由臺北捷運公司營運管理，負責經營管理、保管維護、附屬事業規劃、出租及經營管理等業務。基地北側臨基河路，東側臨美崙公園，南側臨科學教育館，西側預留未來銜接社子輕軌的連接處。附近有天文館、科學教育館、美崙公園以及河濱公園，讓民眾除了可以使用兒童新樂園內的各項遊樂設備外，更可以讓活動動線延伸，創造出一個多元化的享受教育及大自然的兒童新樂園，歡迎前往享受美麗的時光。"
        6 -> "寶藏巖位於台北公館附近，一個在小觀音山南側半山腰上的社區，早期60、70年代為違章建築所形成的聚落，主要由退休的老兵所組成；《寶藏巖》內有座市定古蹟的寶藏巖寺，也是因此得名的。現在的《寶藏巖》有14個藝術家工作間，不定期會舉辦活動展覽等，除了有藝術家駐村之外，這裡在每年的3~5月也會舉辦「光節」，在夜間開展，讓大家欣賞夜晚中的《寶藏巖》。"
        7 -> "故宮收藏的文物珍寶，是舉世聞名的文化資產，從帝王收藏到全民共有，傳承有序的國立故宮博物院典藏，其收藏品的年代幾乎囊括了整個中華文化五千餘年未曾中斷的歷史，在世界文明史上獨一無二。博物院內60多萬件收藏品中，大多數是昔日中國皇室的收集品，皇室的收藏則始自1000多年前的宋朝。隨著數位科技廣泛應用在博物館的展覽或硬體設備，歷時三年的正館改建工程，於民國96年（西元2007年）2月改建完工後的新故宮，除了提供煥然一新的展覽手法之外，也增建了數位學習空間，如數位導覽大廳、多媒體放映室、多媒體兒童學習區等，同時持續推動「數位典藏」、「數位博物館」以及「數位學習」等計畫，營造一個無圍牆的博物館。而多樣化的餐飲服務、電影院以及文化商店等等，讓遊客不僅可以參觀古文物、欣賞新藝術，甚至為約會地點，視故宮為生活的一部份。承先啟後，再造新局，改建完工的故宮不僅代表華夏歷史的傳承，展望未來更是與當代社會文化脈絡連結的新起點。"
        8 -> "行天宮又叫恩主公廟，奉祀關聖帝君，關聖帝君（西元162~219年）是中國歷史上有名的戰將，因為他堅守信義，所以被尊為武聖；又由於他很會理財，所以又被尊奉為商業保護神。現址的行天宮在民國56年（西元1967年）建立，廟貌樸素莊嚴。進入前殿，有很多信徒在神明前俯首跪拜，非常虔誠。走過天井，來到正殿，可以看到神明桌上只有鮮花、清茶，沒有其他供品。因為行天宮戒除殺生，禁止供奉牲禮；還特地勸止燒金紙、演戲酬神、叩謝金牌等行為；同時廟堂前也沒設功德箱，不要信徒添香油錢，首創傳統宗教界素心作風。因為一般人認為行天宮很靈驗，前往求神問卜的人很多，連帶的在行天宮的地下商場，也幾乎全部擺滿了算命攤，形成獨有的特色。"
        9 -> "華山1914文化創意產業園區前身是台北酒廠，建築房舍由日本人於1914年創建，早期是日本生產清酒的廠房，後為台北酒廠。民國76年（西元1987年）台北酒廠搬遷後，華山作為酒廠的產業畫下句點，整個廠房是產業建築技術的博物館。民國88年（西元1999年）後，逐漸成為一個多元發展的藝文展演區，是藝文界、團體及個人使用的創作場地，園區內也有多間餐廳、藝廊、文創商店及表演場地，同時提供各項展演場地的租借服務，是台灣文化創意產業的旗艦基地。"
        10 -> "士林夜市為臺北市最具規模的夜市之一，以陽明戲院及慈諴宮為中心，包含了文林路、大東路、大南路等熱鬧街市集結而成，其中士林市場早在西元1910年即已興建，以各種傳統小吃聞名國內外，許多觀光客皆慕名而來，像是大餅包小餅、石頭火鍋或是士林大香腸等，更是已經成為美食地標，無人不曉。由於夜市鄰近許多學區，故以學生為主要的消費族群，價格也比一般商店便宜許多，例如家具、衣飾、相片沖印店或寵物用品等，都有其集散的區域，『情人巷』中的精品店與冰店，更是吸引了不少當地學子以外的顧客前往。士林夜市所含範圍極廣，在曲折巷弄內行走，常有柳暗花明又一村的驚喜，週末假日時前往，更是人山人海，擠得水洩不通，常見一家大小提著大包小包剛採買的日用品，嘴裡還塞著美味小吃，滿足之情溢於言表。"
        else -> " "
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    val navController = rememberNavController()
    MyApp(navController = navController)
}