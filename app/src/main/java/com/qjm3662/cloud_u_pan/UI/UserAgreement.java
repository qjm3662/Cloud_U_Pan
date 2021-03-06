package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.widget.EditText;

import com.qjm3662.cloud_u_pan.R;

public class UserAgreement extends BaseActivity {

    String str = "优云服务协议\n" +
            "\n" +
            "   本协议系由大连理工大学软件学院优云团队与所有使用优云服务的主体（包括但不限于个人、团队等）（以下简称“用户”）对优云服务的使用及相关服务所订立的有效合约。使用优云服务的任何服务即表示接受本协议的全部条款。本协议适用于任何优云服务，包括本协议期限内的用户所使用的各项服务和软件的升级和更新。\n" +
            "\n" +
            "\n" +
            "一、服务内容及使用须知\n" +
            "   优云服务是一个向广大用户提供上传空间和技术的信息存储空间服务平台，通过优云服务技术为用户提供个人数据存储、同步、管理和分享等在线服务。优云服务本身不直接上传、提供内容，对用户传输内容不做任何修改或编辑。\n" +
            "   用户理解，优云服务仅提供相关的网络服务，除此之外与相关网络服务有关的设备（如个人电脑、手机、及其他与接入互联网或移动网有关的装置）及所需的费用（如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费）均应由用户自行负担。\n" +
            "   用户不得滥用优云服务的服务，优云在此郑重提请您注意，任何经由本服务以上传、张贴、发送即时信息、电子邮件或任何其他方式传送的资讯、资料、文字、软件、音乐、音讯、照片、图形、视讯、信息、用户的登记资料或其他资料（以下简称“内容”），无论系公开还是私下传送，均由内容提供者、使用者对其上传、使用行为自行承担责任。优云服务作为信息存储空间服务平台，无法控制经由本服务传送之内容，也无法对用户的使用行为进行全面控制，因此不能保证内容的合法性、正确性、完整性、真实性或品质；您已预知使用本服务时，可能会接触到令人不快、不适当等内容，并同意将自行加以判断并承担所有风险，而不依赖于优云服务。\n" +
            "   若用户使用优云服务的行为不符合本协议，优云在经由通知、举报等途径发现时有权做出独立判断，且有权在无需事先通知用户的情况下立即终止向用户提供部分或全部服务。用户若通过优云服务散布和传播反动、色情或其他违反国家法律、法规的信息，优云服务的系统记录有可能作为用户违反法律法规的证据；因用户进行上述内容在优云服务的上载、传播而导致任何第三方提出索赔要求或衍生的任何损害或损失，由用户自行承担全部责任。\n" +
            "   优云有权对用户使用优云服务网络服务的情况进行监督，如经由通知、举报等 途径发现用户在使用优云服务所提供的网络服务时违反任何本协议的规定，优云有权要求用户改正或直接采取一切优云认为必要的措施（包括但不限于更改或删除用户上载的内容、暂停或终止用户使用网络服务，和/或公示违法或违反本协议约定使用优云服务用户账户的权利）以减轻用户不当行为造成的影响。\n" +
            "\n" +
            "二、所有权\n" +
            "   优云保留对以下各项内容、信息完全的、不可分割的所有权及知识产权：\n" +
            "\n" +
            "   除用户自行上载、传播的内容外，优云服务及其所有元素，包括但不限于所有内容、数据、技术、软件、代码、用户界面以及与其相关的任何衍生作品；\n" +
            "   用户信息；\n" +
            "   用户向优云服务提供的与该平台服务相关的任何信息及反馈。\n" +
            "未经优云同意，上述资料均不得在任何媒体直接或间接发布、播放、出于播放或发布目的而改写或再发行，或者被用于其他任何商业目的。上述资料或其任何部分仅可作为私人用途而保存在某台计算机内。优云服务不就由上述资料产生或在传送或递交全部或部分上述资料过程中产生的延误、不准确、错误和遗漏或从中产生或由此产生的任何损害赔偿，以任何形式向用户或任何第三方负法律、经济责任；\n" +
            "\n" +
            "   优云服务为提供网络服务而使用的任何软件（包括但不限于软件中所含的任何图像、照片、动画、录像、录音、音乐、文字和附加程序、随附的帮助材料）的一切权利均属于该软件的著作权人，未经该软件的著作权人许可，用户不得对该软件进行反向工程（reverse engineer）、反向编译（decompile）或反汇编（disassemble），或以其他方式发现其原始编码，以及实施任何涉嫌侵害著作权的行为。\n" +
            "\n" +
            "\n" +
            "三、承诺与保证\n" +
            "   用户保证，其向优云服务上传的内容不得并禁止直接或间接的：\n" +
            "删除、隐匿、改变优云服务上显示或其中包含的任何专利、版权、商标或其他所有权声明；\n" +
            "   以任何方式干扰或企图干扰优云服务或功能的正常运行；\n" +
            "避开、尝试避开或声称能够避开任何内容保护机制或者优云服务数据度量工具；\n" +
            "   未获得优云事先书面同意以书面格式或图形方式使用源自优云的任何注册或未注册的作品、服务标志、公司徽标(LOGO)、URL或其他标志；\n" +
            "使用任何标志，包括但不限于以对此类标志的所有者的权利的玷污、削弱和损害的方式使用优云标志，或者以违背本协议的方式为自己或向其他任何人设定或声明设定任何义务或授予任何权利或权限，除非优云以书面方式指明，否则，用户不得导出任何用户信息，并且必须在获取任何用户信息或其他优云服务内容后的 24 小时内停止使用和删除它们；\n" +
            "   未事先经过原始用户的同意向任何非原始用户显示或以其他方式提供任何用户信息；\n" +
            "   请求、收集、索取或以其他方式从任何用户那里获取对优云帐号、密码或其他身份验证凭据的访问权；\n" +
            "   为任何用户自动登录到优云帐号代理身份验证凭据；\n" +
            "   提供跟踪功能，包括但不限于识别其他用户在个人主页上查看或操作；\n" +
            "   自动将浏览器窗口定向到其他网页；\n" +
            "   未经授权冒充他人或获取对优云服务的访问权；或者未经用户明确同意，让任何其他人亲自识别该用户。\n" +
            "   用户违反上述任何一款的保证，优云均有权就其情节，对其做出警告、屏蔽、直至取消资格的处罚；如因用户违反上述保证而给优云服务、优云服务用户或优云的任何合作伙伴造成损失，用户自行负责承担一切法律责任并赔偿损失。\n" +
            "   用户的承诺：\n" +
            "   其一经取得利用优云服务提供的网络服务上传、发布、传送或通过其他方式传播的内容的权利人（如有）的书面授权，并已与前述权利人就权益分配达成内部协议，保证其在将相关内容提交、上传至优云服务前拥有充分、完整无瑕疵、排他的所有权及知识产权。\n" +
            "   用户利用优云服务提供的网络服务上传、发布、传送或通过其他方式传播的内容，不得含有任何违反国家法律法规政策的信息，包括但不限于下列信息：\n" +
            "   反对宪法所确定的基本原则的；\n" +
            "   危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；\n" +
            "   损害国家荣誉和利益的；\n" +
            "   煽动民族仇恨、民族歧视，破坏民族团结的；\n" +
            "   破坏国家宗教政策，宣扬邪教和封建迷信的；\n" +
            "   散布谣言，扰乱社会秩序，破坏社会稳定的；\n" +
            "   散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；\n" +
            "   侮辱或者诽谤他人，侵害他人合法权益的；\n" +
            "   含有法律、行政法规禁止的其他内容的。\n" +
            "   用户不得为任何非法目的而使用本网络服务系统；不得以任何形式使用优云存储网络服务侵犯优云的商业利益，包括并不限于发布非经优云许可的商业广告；不得利用优云服务网络服务系统进行任何可能对互联网或移动网正常运转造成不利影响的行为；\n" +
            "   用户不得利用优云服务的服务从事以下活动：\n" +
            "   未经允许，进入计算机信息网络或者使用计算机信息网络资源的；\n" +
            "   未经允许，对计算机信息网络功能进行删除、修改或者增加的；\n" +
            "   未经允许，对进入计算机信息网络中存储、处理或者传输的数据和应   用程序进行删除、修改或者增加的；\n" +
            "   故意制作、传播计算机病毒等破坏性程序的；\n" +
            "   其他危害计算机信息网络安全的行为。\n" +
            "   如因用户利用优云服务提供的网络服务上传、发布、传送或通过其他方式传播的内容存在权利瑕疵或侵犯了第三方的合法权益（包括但不限于专利权、商标权、著作权及著作权邻接权、肖像权、隐私权、名誉权等）而导致优云面临任何投诉、举报、质询、索赔、诉讼；或者使优云因此遭受任何名誉、声誉或者财产上的损失，用户应积极地采取一切 可能采取的措施，以保证优云免受上述索赔、诉讼的影响。同时用户对优云因此遭受的直接及间接经济损失负有全部的损害赔偿责任。\n" +
            "\n" +
            "四、知识产权保护\n" +
            "   如果用户上传的内容允许其他用户下载、查看、收听或以其他方式访问或分发，其必须保证该内容的发布和相关行为实施符合相关知识产权法律法规中相关的版权政策，包括但不限于：\n" +
            "\n" +
            "   用户在收到侵权通知之时，应立即删除或禁止访问声明的侵权内容，并同时联系递送通知的人员以了解详细信息。\n" +
            "用户知悉并同意优云将根据相关法律法规对第三方发出的合格的侵权通知进行处理，并按照要求删除或禁止访问声明的侵权内容，采用并实施适当的政策，以期杜绝在相应条件下重复侵权。\n" +
            "\n" +
            "五、隐私保护\n" +
            "   优云充分尊重用户个人信息的保护，优云的隐私保护声明列出了优云服务收集和使用您的个人信息应遵循的相关政策和程序。\n" +
            "分享的信息。您可以主动设置与他人分享信息。用户了解并知晓，当用户公开分享信息时，包括优云在内的各种搜索引擎可能会抓取这些信息。\n" +
            "优云不会公开或向第三方提供用户存储在优云服务上的非公开内容，除非有下列情况：\n" +
            "有关法律、法规规定或优云服务合法服务程序规定；\n" +
            "在紧急情况下，为维护用户及公众的权益；\n" +
            "为维护优云的商标权、专利权及其他任何合法权益；\n" +
            "其他依法需要公开、编辑或透露个人信息的情况。\n" +
            "\n" +
            "六、免责声明\n" +
            "   鉴于网络服务的特殊性，用户同意优云服务有权随时变更、中断或终止部分或全部 的网络服务。如变更、中断或终止的网络服务属于免费网络服务，优云服务无需通知用户，也无需对任何用户或任何第三方承担任何责任。\n" +
            "   用户理解，优云服务需要定期或不定期地对提供网络服务的平台或相关的设备进行 检修或者维护，如因此类情况而造成收费网络服务在合理时间内的中断，优云服务无需为此承担任何责任，但优云服务应尽可能事先进行通告。\n" +
            "   优云服务可在任何时候为任何原因变更本服务或删除其部分功能。优云服务可在任何时候取消或终止对用户的服务。优云服务取消或终止服务的决定不需要理由或通知用户。一旦服务取消，用户使用本服务的权利立即终止。 一旦本服务取消或终止，用户在本服务中储存的任何信息可能无法恢复。\n" +
            "优云服务不保证（包括但不限于）：\n" +
            "   优云服务适合用户的使用要求；\n" +
            "   优云服务不受干扰，及时、安全、可靠或不出现错误；及用户经由优云服务取得的任何产品、服务或其他材料符合用户的期望。\n" +
            "   用户使用经由优云服务下载或取得的任何资料，其风险由用户自行承担；因该等使用导致用户电脑系统损坏或资料流失，用户应自己负完全责任；\n" +
            "   基于以下原因而造成的利润、商业信誉、资料损失或其他有形或无形损失， 优云服务不承担任何直接、间接的赔偿：\n" +
            "   对优云服务的使用或无法使用；\n" +
            "   经由优云服务购买或取得的任何产品、资料或服务；\n" +
            "   用户资料遭到未授权的使用或修改；及其他与优云服务相关的事宜。\n" +
            "   由于用户授权第三方（包括第三方应用）访问/使用其优云服务空间的内容所导致的纠纷或损失，应由用户自行负责，与优云无关。\n" +
            "\n" +
            "七、其他\n" +
            "   本协议最终解释权归优云所有。\n" +
            "   本协议一经公布即生效，优云有权随时对协议内容进行修改，修改后的结果公布于优云服务网站上。如果不同意优云服务对本协议相关条款所做的修改，用户有权停止使用网络服务。如果用户继续使用网络服务，则视为用户接受优云对本协议相关条款所做的修改。\n" +
            "   本协议项下优云服务对于用户所有的通知均可通过网页公告、电子邮件、手机短信或常规的信件传送等方式进行；该等通知于发送之日视为已送达收件人。\n" +
            "   本协议的订立、执行和解释及争议的解决均应适用中国法律并受中国法院管辖。如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；协商不成时，任何一方均可向优云所在地的人民法院提起诉讼。\n" +
            "   本协议构成双方对本协议之约定事项及其他有关事宜的完整协议，除本协议规定的之外，未赋予本协议各方其他权利。\n" +
            "如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。\n" +
            "\n" +
            "文明上网七条底线\n" +
            "    根据我国网络发展的现状，国信办鲁炜主任提出了网络空间的“七条底线”，我们每位网民在自由表达自己意见和诉求的同时，也要遵守“七条底线”，文明上网，争做文明网民。\n" +
            "\n" +
            "七条底线\n" +
            "   1.法律法规底线：有法可依、有法必依、执法必严、违法必究，任何时候，无论是网上网下，都将始终做到违法必究；\n" +
            "   2.社会主义制度底线，为我们全面建成小康社会提供了有力地制度保障，我们要积极拥护社会主义及社会主义制度；\n" +
            "   3.国家利益底线，作为国家公民，时刻维护我们伟大祖国的利益，这也是宪法赋予我们每位公民的光荣义务；\n" +
            "   4.公民合法权益底线，我们在网络反腐的同时，切记不能以“艳照”等不健康、不正当甚至违法手段对别人进行人身攻击，否则不仅触犯法律，也侵犯了无辜者的合法权益；\n" +
            "   5.社会公共秩序底线，网络世界必须也要遵循一定的秩序规则，唯有如此大家才能营造一个良好健康的网络环境；\n" +
            "   6.道德风尚底线，崇尚美德在我国延续几千年的优秀传统，网络空间里也要讲道德，不做有违道德之事；\n" +
            "7.信息真实性底线，要求我们在上网时一定要实事求是，而不能以讹传讹、散发谣传，积极宣传政府部门发布的真实信息。\n" +
            "\n" +
            "   七条底线是根本，不能突破；是方圆，不能逾越。我们广大网民积极宣传落实“七条底线”，争做文明网民，净化网络空间，还网络一片“蓝天”。";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        EditText editText = (EditText) findViewById(R.id.content);

        editText.setText(str);

    }
}
