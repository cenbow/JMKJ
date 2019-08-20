
package com.cn._gen.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.cn.jm.core.db.IMappingKit;

/**
 * Generated by 小跑科技robot.
 */
public class _BaseMappingKit implements IMappingKit{

	@Override
		public void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("shop_address", "id", Address.class);
		arp.addMapping("shop_bank", "id", Bank.class);
		arp.addMapping("shop_bank_account", "id", BankAccount.class);
		arp.addMapping("shop_cash", "id", Cash.class);
		arp.addMapping("shop_gift", "id", Gift.class);
		arp.addMapping("shop_gift_give", "id", GiftGive.class);
		arp.addMapping("shop_goods", "id", Goods.class);
		arp.addMapping("shop_goods_cart", "id", GoodsCart.class);
		arp.addMapping("shop_goods_label", "id", GoodsLabel.class);
		arp.addMapping("shop_goods_models", "id", GoodsModels.class);
		arp.addMapping("shop_goods_resale", "id", GoodsResale.class);
		arp.addMapping("shop_goods_star", "id", GoodsStar.class);
		arp.addMapping("shop_label_relations", "id", LabelRelations.class);
		arp.addMapping("shop_marketing", "id", Marketing.class);
		arp.addMapping("shop_marketing_goods", "id", MarketingGoods.class);
		arp.addMapping("shop_order", "id", Order.class);
		arp.addMapping("shop_order_goods", "id", OrderGoods.class);
		arp.addMapping("shop_payments", "id", Payments.class);
		arp.addMapping("shop_recharge", "id", Recharge.class);
		arp.addMapping("shop_refund_goods", "id", RefundGoods.class);
		arp.addMapping("shop_refund_order", "id", RefundOrder.class);
		arp.addMapping("shop_search_history", "id", SearchHistory.class);
		arp.addMapping("shop_sku", "id", Sku.class);
		arp.addMapping("shop_sku_attr", "id", SkuAttr.class);
		arp.addMapping("shop_spec", "id", Spec.class);
		arp.addMapping("shop_spec_attribute", "id", SpecAttribute.class);
		arp.addMapping("shop_spec_group", "id", SpecGroup.class);
		arp.addMapping("shop_spec_models", "id", SpecModels.class);
		arp.addMapping("shop_user_collect", "id", UserCollect.class);
		arp.addMapping("system_menu", "id", Menu.class);
		arp.addMapping("system_power", "id", Power.class);
		arp.addMapping("system_role", "id", Role.class);
		arp.addMapping("system_role_account", "id", RoleAccount.class);
		arp.addMapping("system_role_menu", "id", RoleMenu.class);
		arp.addMapping("system_role_power", "id", RolePower.class);
		arp.addMapping("tb_account", "id", Account.class);
		arp.addMapping("tb_account_session", "id", AccountSession.class);
		arp.addMapping("tb_account_user", "id", AccountUser.class);
		arp.addMapping("tb_app_version", "id", AppVersion.class);
		arp.addMapping("tb_area", "id", Area.class);
		arp.addMapping("tb_balance_record", "id", BalanceRecord.class);
		arp.addMapping("tb_configure", "id", Configure.class);
		arp.addMapping("tb_content", "id", Content.class);
		arp.addMapping("tb_extract_log", "id", ExtractLog.class);
		arp.addMapping("tb_freight", "id", Freight.class);
		arp.addMapping("tb_freight_area", "id", FreightArea.class);
		arp.addMapping("tb_freight_template", "id", FreightTemplate.class);
		arp.addMapping("tb_img", "id", Img.class);
		arp.addMapping("tb_label", "id", Label.class);
		arp.addMapping("tb_label_relation", "id", LabelRelation.class);
		arp.addMapping("tb_no", "id", No.class);
		arp.addMapping("tb_notice", "id", Notice.class);
		arp.addMapping("tb_notice_account", "id", NoticeAccount.class);
		arp.addMapping("tb_options", "id", Options.class);
		arp.addMapping("tb_problem", "id", Problem.class);
		arp.addMapping("tb_recharge_order", "id", RechargeOrder.class);
		arp.addMapping("tb_suggest", "id", Suggest.class);
		arp.addMapping("tb_zan", "id", Zan.class);
		arp.addMapping("webcast_associate_request", "id", AssociateRequest.class);
		arp.addMapping("webcast_broadcast_request", "id", BroadcastRequest.class);
		arp.addMapping("webcast_report", "id", Report.class);
		arp.addMapping("webcast_room", "id", Room.class);
		arp.addMapping("webcast_room_account", "id", RoomAccount.class);
		arp.addMapping("webcast_room_management", "id", RoomManagement.class);
		arp.addMapping("webcast_room_merchant", "id", RoomMerchant.class);
		arp.addMapping("webcast_room_record", "id", RoomRecord.class);
	}

}