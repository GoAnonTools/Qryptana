{-# LANGUAGE DuplicateRecordFields #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE GADTs #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE ScopedTypeVariables #-}
{-# LANGUAGE StandaloneDeriving #-}
{-# LANGUAGE TypeSynonymInstances #-}
{-# OPTIONS_GHC -Wno-orphans #-}
{-# OPTIONS_GHC -fno-warn-ambiguous-fields #-}

module RandomServers where

import Control.Monad (replicateM)
import Data.Foldable (foldMap')
import Data.List (sortOn)
import Data.List.NonEmpty (NonEmpty)
import Data.Monoid (Sum (..))
import Simplex.Chat (defaultChatConfig, chooseRandomServers)
import Simplex.Chat.Controller (ChatConfig (..), PresetServers (..))
import Simplex.Chat.Operators
import Simplex.Messaging.Agent.Env.SQLite (ServerRoles (..))
import Simplex.Messaging.Protocol (ProtoServerWithAuth (..), SProtocolType (..), UserProtocol)
import Test.Hspec

randomServersTests :: Spec
randomServersTests = describe "choosing preset servers" $ do
  it "should use the single Qryptana SMP preset server" testPresetSMPServers
  it "should use the single Qryptana XFTP preset server" testPresetXFTPServers

deriving instance Eq ServerRoles

deriving instance Eq (UserServer' s p)

testPresetSMPServers :: IO ()
testPresetSMPServers = do
  [srvs1, srvs2, srvs3] <-
    replicateM 3 $
      checkEnabled SPSMP 1 True =<< chooseRandomServers (presetServers defaultChatConfig)
  (srvs1 == srvs2 && srvs2 == srvs3) `shouldBe` True

testPresetXFTPServers :: IO ()
testPresetXFTPServers = do
  [srvs1, srvs2, srvs3] <-
    replicateM 3 $
      checkEnabled SPXFTP 1 True =<< chooseRandomServers (presetServers defaultChatConfig)
  (srvs1 == srvs2 && srvs2 == srvs3) `shouldBe` True

checkEnabled :: UserProtocol p => SProtocolType p -> Int -> Bool -> NonEmpty (PresetOperator) -> IO [NewUserServer p]
checkEnabled p n allUsed presetOps' = do
  let PresetServers {operators = presetOps} = presetServers defaultChatConfig
      presetSrvs = sortOn server' $ concatMap (pServers p) presetOps
      srvs' = sortOn server' $ concatMap (pServers p) presetOps'
      Sum toUse = foldMap' (Sum . operatorServersToUse p) presetOps
      Sum toUse' = foldMap' (Sum . operatorServersToUse p) presetOps'
  length presetOps `shouldBe` length presetOps'
  toUse `shouldBe` toUse'
  srvs' == presetSrvs `shouldBe` allUsed
  map enable srvs' `shouldBe` map enable presetSrvs
  let enbldSrvs = filter (\UserServer {enabled} -> enabled) srvs'
  toUse `shouldBe` n
  length enbldSrvs `shouldBe` n
  pure enbldSrvs
  where
    server' UserServer {server = ProtoServerWithAuth srv _} = srv
    enable :: forall p. NewUserServer p -> NewUserServer p
    enable srv = (srv :: NewUserServer p) {enabled = False}
