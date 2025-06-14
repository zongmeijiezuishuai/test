package com.example.myapplication.entity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DataService {
    public static List<ProductInfo> getListData(int position){
        List<ProductInfo> list =new ArrayList<>();

        //后面一定要检查运行后效果，看是否需要加\n！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        if(position==0){
            //---------------------红茶------------------------------
            list.add(new ProductInfo(1001, R.drawable.hongcha,"2024年新茶特级武夷正山小种花果香浓香型红茶金骏眉500克","这款2024年新茶特级武夷正山小种金骏眉红茶，选自福建省武夷山正山地区的优质茶叶，采用传统工艺精心制作，茶叶采摘自高山优质茶树。金骏眉红茶以其独特的花果香气和浓郁的茶香风味深受茶友喜爱。每一片茶叶均经过严格筛选，确保茶汤清澈透亮，口感醇厚细腻。\n" +
                    "产地：福建泉州，净含量：500g\n",110));

            list.add(new ProductInfo(1002, R.drawable.bojuehongcha,"英国伯爵红茶奶茶店专用原材料英式格雷佛手柑特调高香特价500g","这款英国伯爵红茶奶茶店专用原材料采用精选英式格雷佛（Earl Grey）红茶，特调高香的佛手柑精华，为您提供一款充满经典英式风味的茶叶原料。独特的柑橘香气与红茶的浓郁底味完美融合，是奶茶店、咖啡馆以及茶饮店等饮品店铺的理想选择。\n" +
                    "产地：福建泉州，净含量：500g\n",29));

            list.add(new ProductInfo(1003, R.drawable.naicha,"日月潭蜜香红茶奶茶店专用原料烤奶红茶奶盖茶茶叶商用茶散装500g","这款日月潭蜜香红茶奶茶店专用原料选用源自台湾日月潭地区的优质蜜香红茶，独特的蜜香味与红茶的醇厚底味相结合，打造出口感层次丰富的茶饮原料。特别调制的烤奶红茶及奶盖茶，是奶茶店、茶饮店的理想选择，适合多种经典饮品的制作，如奶盖红茶、蜜香奶茶等，助力茶饮店在市场中脱颖而出。\n" +
                    "产地：福建泉州，净含量：500g\n",59));

            list.add(new ProductInfo(1004, R.drawable.mulintingfeng,"东方红韵210g*2礼盒装沐林听风茶叶紫金红茶客家茶河源高山云雾茶","东方红韵是一款源自广东河源高山云雾地区的精致红茶，结合了传统茶艺与现代制茶工艺，完美呈现出紫金红茶独特的浓郁香气与甘甜口感。这款沐林听风茶采用精选茶叶，通过手工精制而成，茶汤色泽红亮，香气悠长，是对红茶爱好者的一次深度味觉体验。礼盒装设计更是彰显高雅与品味，非常适合作为礼品赠送，传递健康与尊重。\n" +
                    "产地：广东河源，净含量：420g礼盒装（210g*2礼盒）\n",278));

            list.add(new ProductInfo(1005, R.drawable.zijincanhong,"紫金蝉茶150g每罐河源高山云雾茶广东茶叶客家茶斗记红茶蜜香味","紫金蝉茶是一款精选自广东河源地区高山云雾茶园的优质红茶。它以其天然的蜜香味和丰富的茶汤层次，深受茶友的喜爱。这款红茶融合了客家传统茶艺和现代制茶工艺，具有浓郁的花香和蜜香，口感醇厚甘爽，是品茗爱好者不可错过的好茶。\n" +
                    "产地：广东河源，净含量：150g\n",238));

            list.add(new ProductInfo(1006, R.drawable.yinghong,"【2024明春茶】有机红茶英红九号英德红茶特级浓香型红茶茶叶150g","2024明春茶 有机红茶 英红九号，精选自广东英德的优质茶叶，属于特级浓香型红茶。采用无农药、无化肥的有机种植方式，从茶园到茶杯，整个过程都严格把控，确保茶叶的天然、纯净与高品质。这款英红九号红茶，香气浓郁，口感醇厚，带有独特的蜜香与果香，是红茶爱好者的上佳选择。\n" +
                    "产地：广东河源，净含量：150g\n",158));
        } else if (position==1) {
            //---------------------绿茶-----------------------------
            list.add(new ProductInfo(1007, R.drawable.longjing,"2024新茶西湖牌明前特级西湖龙井茶150g绿茶茶叶礼盒装送长辈","2024年全新上市的西湖牌明前特级西湖龙井茶，选取西湖龙井产区最优质的茶叶，采摘自春季“明前”时节。此款茶叶来自西湖龙井的核心产区，具有色泽翠绿、香气清新、滋味甘醇的特点，茶汤清澈明亮，口感清爽回甘，堪称中国绿茶的代表。精美的礼盒装设计，是送礼自饮的理想选择，传递着品位与心意。\n" +
                    "产地：浙江杭州，净含量：150g\n",398));

            list.add(new ProductInfo(1008, R.drawable.biluochun,"三万昌碧螺春2024新茶雨前一级绿茶苏州洞庭原产送礼盒送长辈茶叶","三万昌碧螺春雨前一级绿茶，来自江苏苏州洞庭山的优质茶园。碧螺春被誉为中国十大名茶之一，以其独特的香气、清新的口感和细腻的茶叶著称。此款碧螺春采摘自雨前时节，选取茶树的新芽，茶叶鲜嫩，滋味更加清香。精美的礼盒包装非常适合送长辈、送亲友、商务赠礼，表达心意与尊重，呈现出高雅的品味。\n" +
                    "产地：江苏苏州，净含量：250g\n",269));

            list.add(new ProductInfo(1009, R.drawable.maojian,"黄山毛峰绿茶2024新茶安徽高山明前特级毛尖嫩芽春茶散装茶叶250g","黄山毛峰绿茶，采自安徽黄山的高山茶园，属于明前特级茶叶，精选春季嫩芽，茶质鲜嫩，香气独特，味道醇厚。黄山毛峰茶以其独特的山韵、清新的香气和鲜美的口感在中国茶叶中占有一席之地。这款茶叶是从黄山茶区的高海拔区域采摘而来，采用传统工艺精心制作而成，带来绝佳的茶香体验，是喜欢绿茶消费者的理想选择。\n" +
                    "产地：安徽黄山，净含量：125g\n",57));

            list.add(new ProductInfo(1010, R.drawable.ya,"嫩芽正宗雀舌绿茶2024明前特级新茶贵州湄潭翠芽毛尖高山云雾茶叶","这款2024年明前特级雀舌绿茶来自贵州湄潭的高山云雾茶园，采用春季最嫩的茶芽采摘而成，茶叶选材精良，口感清新、滋味鲜美。明前茶是指清明节前采摘的茶叶，茶芽鲜嫩，富含天然营养成分，是茶中的上品。湄潭翠芽和毛尖茶的结合，使得这款绿茶兼具清香与甘甜，回味悠长，适合喜爱绿茶的消费者。\n" +
                    "产地：贵州遵义，净含量：250g\n",108));

            list.add(new ProductInfo(1011, R.drawable.lvshan,"明前特级庐山云雾茶礼盒装 2024新茶 送礼绿茶茶叶高山云雾108g","这款明前特级庐山云雾茶来自中国著名的茶叶产区——庐山，采摘自清明节前的嫩芽，因其生长在云雾缭绕的高山上，茶叶吸收了山间的清新气息与矿物质，赋予了它独特的香气与口感。2024年新茶，精选最上等的茶叶，包装精美，适合送礼或自用，体验一杯来自庐山高山云雾间的鲜美茶香。\n" +
                    "产地：江西南昌，净含量：108g\n",188));

            list.add(new ProductInfo(1012, R.drawable.liuan,"徽将军2024新茶绿茶特级六安瓜片茶叶礼盒装送长辈300g","这款徽将军特级六安瓜片来自中国著名茶叶产区——安徽六安，是六安瓜片中的顶级精选。选用2024年新茶，手工采摘，品质卓越，茶汤清澈透亮，滋味鲜爽甘醇。六安瓜片素有“绿茶之王”的美誉，是中国十大名茶之一，广受茶友喜爱。此款茶叶包装精美，适合送礼，尤其是送给长辈、亲友或商务合作伙伴，体现您的心意与品位。\n" +
                    "产地：安徽六安，净含量：300g\n",249));

            list.add(new ProductInfo(1013, R.drawable.taiping,"太平猴魁2024新茶特一级高山云雾春茶安徽黄山茶叶绿茶高端礼盒装","太平猴魁是中国绿茶中的佼佼者，拥有“茶中珍品”的美誉，产自中国著名的茶叶产区——安徽黄山。这款太平猴魁2024新茶，选用高山云雾中的嫩叶，采用精湛的制茶工艺，打造出一款色香味俱佳的特级绿茶。茶叶呈现出嫩绿、挺拔的形态，茶汤清亮、滋味鲜醇，带有独特的花香和栗香，回味甘甜，深受茶友青睐。礼盒包装高端大气，适合各类场合赠送，是您表达心意、彰显品位的绝佳选择。\n" +
                    "产地：安徽芜湖，净含量：250g\n",106));
        } else if (position==2) {
            //---------------------青茶------------------------------
            list.add(new ProductInfo(1014, R.drawable.tieguanyin,"安溪铁观音茶叶2024新茶特级乌龙茶官方旗舰店罐装正品500g","安溪铁观音是中国著名的乌龙茶之一，源自福建省安溪县，因其香气高扬、滋味醇厚、回甘悠长而闻名天下。2024年的新茶，采摘自安溪的优质茶园，经过传统工艺精心制作，呈现出铁观音独特的花香、果香和蜜香，每一口茶汤都带来丰富的层次感和回味。作为特级乌龙茶，这款铁观音茶叶品质上乘，茶叶鲜嫩、茶汤清亮、口感甘醇，是乌龙茶爱好者不可错过的珍品。\n" +
                    "产地：福建泉州，净含量：500g\n",148));

            list.add(new ProductInfo(1015, R.drawable.fenghuang,"凤凰单丛茶高山老枞水仙浓香型乌龙茶正宗潮州茶叶特产2024春茶新","凤凰单丛茶是中国著名的乌龙茶之一，产自广东省潮州市的凤凰山。凤凰单丛茶以其独特的香气和醇厚的口感著称，其中以高山老枞水仙为代表，深受茶友们的喜爱。2024年春季新茶，采摘自凤凰山的优质茶园，经过传统工艺精心制作，完美展现了水仙品种的浓香特质，茶汤滋味浓烈且细腻，回甘持久。无论是喜欢浓香型乌龙茶的茶友，还是注重品质的品茶人士，这款凤凰单丛茶都能给您带来独特的品茗体验。\n" +
                    "产地：广东潮州， 净含量：500g\n",58));

            list.add(new ProductInfo(1016, R.drawable.dongfang,"【东方美人头等奖】台湾原装75克罐装白毫乌龙茶冷泡送礼花蜜香甜","东方美人茶，作为台湾最具代表性的茶叶之一，素以花香、蜜香以及浓郁的甘甜回甘著称。这款白毫乌龙茶（又称为“东方美人”）采用台湾优质茶园的新鲜茶叶，手工采摘，选料精细，制茶工艺独特。冷泡后茶汤清澈，香气馥郁，带有明显的花蜜香甜，十分适合作为夏季饮品或赠送亲友的高端礼品。\n" +
                    "产地：广东潮州， 净含量：50g\n",388));

            list.add(new ProductInfo(1017, R.drawable.da,"【大禹岭-非凡大师】限量臻品私房75g花香浓醇清冽台湾高山乌龙茶","大禹岭-非凡大师”是台湾高山乌龙茶中的佼佼者，选用台湾大禹岭地区的优质茶叶，通过精湛的工艺制作而成，带有花香、浓醇、清冽的独特口感，深受茶友们的喜爱。每一片茶叶都经过严格筛选，确保其高山茶的独特风味，口感圆润，滋味鲜活。限量制作的私房茶，仅有少量供应，展现出台湾高山乌龙茶的珍贵与精致。\n" +
                    "产地：河南郑州， 净含量：75g\n",759));

            list.add(new ProductInfo(1018, R.drawable.a,"阿里山金萱奶香乌龙茶无添加礼盒台湾原装冬茶罐装150g清香高山茶","阿里山金萱奶香乌龙茶是台湾著名的高山茶之一，产自台湾阿里山地区。由于其得天独厚的气候和土壤条件，阿里山的茶叶具有极其独特的风味——香气馥郁、口感醇厚，带有天然的奶香味，深受茶友喜爱。阿里山金萱茶以其独特的“奶香”风味和清新的口感，成为台湾高山茶的代表之一。此款产品采用冬茶（冬季采摘的茶叶），在寒冷季节生长的茶叶通常更加饱满，香气更加浓郁，回甘丰富\n" +
                    "产地：河南郑州， 净含量：150g\n",140));

            list.add(new ProductInfo(1019, R.drawable.heiwulong,"新凤鸣油切黑乌龙茶高山乌龙茶中国台湾进口特级高山茶茶叶冷泡茶","新凤鸣油切黑乌龙茶是台湾著名的高山乌龙茶之一，以其特有的油切效果和丰富的茶香，受到广大茶友的喜爱。这款茶采用高山乌龙茶作为基础，精心打造出独具风味的黑乌龙茶，并且适合冷泡，特别适合现代人快节奏生活中的饮用需求。黑乌龙茶在提升茶香浓郁度的同时，还具备独特的油切效果，帮助消化和代谢，成为茶叶中的健康之选。\n" +
                    "产地：中国香港， 净含量：100g\n",75));
        } else if (position==3) {
            //---------------------黄茶------------------------------
            list.add(new ProductInfo(1020, R.drawable.junshan,"湖南岳阳君山银针2024明前茶黄茶芽头茶潇湘牌100克罐装","君山银针黄茶，产自湖南岳阳君山岛，是中国黄茶中的珍品，以其独特的制茶工艺和高品质茶叶在国内外茶叶市场中备受推崇。该款茶叶来自2024年的明前茶，是采摘自君山岛上芽头茶的上等黄茶，茶汤色泽金黄清澈，滋味鲜醇、甘甜，带有一丝清香与花香，口感细腻，回甘悠长，是黄茶爱好者和茶文化收藏者的珍贵之选。\n" +
                    "产地：湖南长沙， 净含量：100g\n",198));

        } else if (position==4) {
            //---------------------黑茶---------------------------------
            list.add(new ProductInfo(1021, R.drawable.puer,"宫明茶叶 2023年云南普洱茶头春冰岛甜香古树普洱生茶礼盒装500克","这款来自宫明茶叶的2023年云南普洱茶头春冰岛甜香古树普洱生茶，是普洱茶爱好者不容错过的优质茶品。选用云南名茶产区——冰岛地区的古树茶叶制作而成，茶叶采摘自2023年春季的头春茶，茶质上乘，香气浓郁、口感甘甜、回甘持久，茶汤清澈透亮。它代表了云南普洱茶的传统与创新，适合珍藏和品饮，是送礼和自用的上佳之选。\n" +
                    "产地：云南昆明， 净含量：500g\n",215));

        } else{
            //----------------------白茶----------------------------------
            list.add(new ProductInfo(1022, R.drawable.baihao,"霖慧堂福鼎白茶白毫银针茶叶高山特级正宗明前白毫银针茶叶50g","这款来自宫明茶叶的2023年云南普洱茶头春冰岛甜香古树普洱生茶，是普洱茶爱好者不容错过的优质茶品。选用云南名茶产区——冰岛地区的古树茶叶制作而成，茶叶采摘自2023年春季的头春茶，茶质上乘，香气浓郁、口感甘甜、回甘持久，茶汤清澈透亮。它代表了云南普洱茶的传统与创新，适合珍藏和品饮，是送礼和自用的上佳之选。这款霖慧堂福鼎白茶白毫银针茶叶，采自福建福鼎的高山茶园，精选明前白毫银针，每一根茶芽都包裹着显著的白毫，色泽银亮，茶香清新，口感鲜爽甘甜。作为白茶中的珍品，白毫银针茶叶内含丰富的营养成分，且极具保健功效。这款茶叶是对传统白茶工艺的传承与升华，适合茶友们日常饮用、品鉴及收藏。\n" +
                    "产地：福建宁德， 净含量：150g\n",158));

        }

        return list;
    }

}